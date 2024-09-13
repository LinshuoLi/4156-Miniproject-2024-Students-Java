package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Unit tests for the class.
 */
@SpringBootTest
@ContextConfiguration
class RouteControllerUnitTests {
  MyFileDatabase database;
  Map<String, Department> departmentMapping;
  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new RouteController()).build();

    Map<String, Course> courses = new HashMap<>();
    courses.put("4156", new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250));

    Map<String, Department> departmentMapping = new HashMap<>();
    departmentMapping.put("COMS", new Department("COMS", courses, "Luca Carloni", 2000));

    IndividualProjectApplication.myFileDatabase = new MyFileDatabase(1, "testFilePath");
    IndividualProjectApplication.myFileDatabase.setMapping(departmentMapping);
  }

  @AfterEach
  void tearDown() {
    IndividualProjectApplication.myFileDatabase = null;
  }

  @Test
  void retrieveDepartmentSuccessTest() throws Exception {
    var result = mockMvc.perform(get("/retrieveDept")
            .param("deptCode", "COMS")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(
            "COMS 4156: \nInstructor: Griffin Newbold; Location: 417 IAB; Time: 11:40-12:55\n"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void retrieveDepartmentNotSuccessTest() throws Exception {
    var result = mockMvc.perform(get("/retrieveDept")
            .param("deptCode", "IEOR")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Department Not Found"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void retrieveCourseSuccessTest() throws Exception {
    var res = mockMvc.perform(get("/retrieveCourse")
            .param("deptCode", "COMS")
            .param("courseCode", "4156")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(result -> assertEquals(
            "Instructor: Griffin Newbold; Location: 417 IAB; Time: 11:40-12:55",
            result.getResponse().getContentAsString().trim()));
    assertNotNull(res, "The result should not be null");
  }

  @Test
  void retrieveCourseNotFoundTest() throws Exception {
    var result = mockMvc.perform(get("/retrieveCourse")
            .param("deptCode", "COMS")
            .param("courseCode", "0000")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Course Not Found"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void retrieveNullCourseTest() throws Exception {
    var result = mockMvc.perform(get("/retrieveCourse")
            .param("deptCode", "COMS")
            .param("courseCode", (String) null)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void isCourseFullNotFullTest() throws Exception {
    var result = mockMvc.perform(get("/isCourseFull")
            .param("deptCode", "COMS")
            .param("courseCode", "4156")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void getMajorCountFromDeptTest() throws Exception {
    var result = mockMvc.perform(get("/getMajorCountFromDept")
            .param("deptCode", "COMS")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("There are: 2000 majors in the department"));
    assertNotNull(result, "The result should not be null");
  }


  @Test
  void retrieveDepartmentMissingParamTest() throws Exception {
    var result = mockMvc.perform(get("/retrieveDept")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void retrieveDepartmentWithNonexistTest() throws Exception {
    var result = mockMvc.perform(get("/retrieveDept")
            .param("deptCode", "BIOCHEM")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Department Not Found"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void retrieveCourseMissingParamTest() throws Exception {
    var result = mockMvc.perform(get("/retrieveCourse")
            .param("deptCode", "COMS")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void retrieveCourseInvalidDeptTest() throws Exception {
    var result = mockMvc.perform(get("/retrieveCourse")
            .param("deptCode", "BIOCHEM")
            .param("courseCode", "4156")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Department Not Found"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void isCourseFullInvalidCourseTest() throws Exception {
    var result = mockMvc.perform(get("/isCourseFull")
            .param("deptCode", "COMS")
            .param("courseCode", "0000")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Course Not Found"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void addMajorToDeptSucessTest() throws Exception {
    var result = mockMvc.perform(patch("/addMajorToDept")
            .param("deptCode", "COMS")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Attribute was updated successfully"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void addMjorToDeptNotSucessTest() throws Exception {
    var result = mockMvc.perform(patch("/addMajorToDept")
            .param("deptCode", "NONEXISTENT")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Department Not Found"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void removeMajorFromDeptSucessTest() throws Exception {
    var result = mockMvc.perform(patch("/removeMajorFromDept")
            .param("deptCode", "COMS")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Attribute was updated or is at minimum"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void removeMajorFromDeptNotSucessTest() throws Exception {
    var result = mockMvc.perform(patch("/removeMajorFromDept")
            .param("deptCode", "BIOCHEM")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Department Not Found"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void changeCourseTimeSuccessTest() throws Exception {
    var result = mockMvc.perform(patch("/changeCourseTime")
            .param("deptCode", "COMS")
            .param("courseCode", "4156")
            .param("time", "2:40-3:55")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Attributed was updated successfully."));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void changeCourseTimeNotSuccessTest() throws Exception {
    var result = mockMvc.perform(patch("/changeCourseTime")
            .param("deptCode", "COMS")
            .param("courseCode", "0000")
            .param("time", "2:40-3:55")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Course Not Found"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void changeCourseTeacherTest() throws Exception {
    var result = mockMvc.perform(patch("/changeCourseTeacher")
            .param("deptCode", "COMS")
            .param("courseCode", "4156")
            .param("teacher", "Ansaf Salleb-Aouissi")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Attributed was updated successfully."));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void changeCourseTeacherInvalidCourseTest() throws Exception {
    var result = mockMvc.perform(patch("/changeCourseTeacher")
            .param("deptCode", "COMS")
            .param("courseCode", "0000")
            .param("teacher", "Griffin Newbold")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Course Not Found"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void changeCourseTeacherInvalidDeptTest() throws Exception {
    var result = mockMvc.perform(patch("/changeCourseTeacher")
            .param("deptCode", "BIOCHEM")
            .param("courseCode", "4156")
            .param("teacher", "Griffin Newbold")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Course Not Found"));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void changeCourseLocationSuccessTest() throws Exception {
    var result = mockMvc.perform(patch("/changeCourseLocation")
            .param("deptCode", "COMS")
            .param("courseCode", "4156")
            .param("location", "309 HAV")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Attributed was updated successfully."));
    assertNotNull(result, "The result should not be null");
  }

  @Test
  void changeCourseLocationNotSuccessTest() throws Exception {
    var result = mockMvc.perform(patch("/changeCourseLocation")
            .param("deptCode", "COMS")
            .param("courseCode", "0000")
            .param("location", "309 HAV")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Course Not Found"));
    assertNotNull(result, "The result should not be null");
  }
}