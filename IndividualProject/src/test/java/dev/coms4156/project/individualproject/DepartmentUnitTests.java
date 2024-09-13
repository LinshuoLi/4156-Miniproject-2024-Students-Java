package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Unit tests for the class.
 */
@SpringBootTest
@ContextConfiguration
class DepartmentUnitTests {

  static Department testDepartment;
  static Course sampleCourse;

  @BeforeAll
  static void setUpDepartmentForTesting() {
    sampleCourse = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
    Map<String, Course> courses = new HashMap<>();
    courses.put("COMS4156W", sampleCourse);
    testDepartment = new Department("COMS", courses, "Luca Carloni", 10);
  }

  @Test
  void getDepartmentChairTest() {
    assertEquals("Luca Carloni", testDepartment.getDepartmentChair());
  }

  @Test
  void getCourseSelectionTest() {
    Course course = testDepartment.getCourseSelection().get("COMS4156W");
    assertEquals(sampleCourse, course);
  }

  @Test
  void addPersonToMajorTest() {
    Course course = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
    Map<String, Course> courses = new HashMap<>();
    courses.put("COMS4156W", course);
    Department department = new Department("COMS", courses, "Luca Carloni", 10);
    department.addPersonToMajor();
    assertEquals(11, department.getNumberOfMajors());
  }

  @Test
  void dropPersonFromMajorTest() {
    testDepartment.dropPersonFromMajor();
    assertEquals(9, testDepartment.getNumberOfMajors());
  }

  @Test
  void dropPersonFromMajorFailedTest() {
    Course course = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
    Map<String, Course> courses = new HashMap<>();
    courses.put("COMS4156W", course);
    Department department = new Department("COMS", courses, "Luca Carloni", 0);
    department.dropPersonFromMajor();
    assertEquals(0, department.getNumberOfMajors());
  }

  @Test
  void addCourseTest() {
    Course newCourse = new Course("Ansaf Salleb-Aouissi", "301 URIS", "10:10-11:25", 250);
    testDepartment.addCourse("COMS4701W", newCourse);
    assertEquals(newCourse, testDepartment.getCourseSelection().get("COMS4701W"));
  }

  @Test
  void createCourseTest() {
    testDepartment.createCourse("COMS4705W", "Zhou Yu", "CSB 451", "4:10-5:25", 100);
    Course createdCourse = testDepartment.getCourseSelection().get("COMS4705W");
    assertEquals("Zhou Yu", createdCourse.getInstructorName());
    assertEquals("CSB 451", createdCourse.getCourseLocation());
    assertEquals("4:10-5:25", createdCourse.getCourseTimeSlot());
    assertEquals(100, createdCourse.enrollmentCapacity());
  }

  @Test
  void toStringTest() {
    Course course = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
    Map<String, Course> courses = new HashMap<>();
    courses.put("COMS4156W", course);
    Department department = new Department("COMS", courses, "Luca Carloni", 0);
    String expectedResult = "COMS COMS4156W: \nInstructor: Griffin Newbold; Location: 417 IAB; "
        + "Time: 11:40-12:55\n";
    assertEquals(expectedResult, department.toString());
  }

}