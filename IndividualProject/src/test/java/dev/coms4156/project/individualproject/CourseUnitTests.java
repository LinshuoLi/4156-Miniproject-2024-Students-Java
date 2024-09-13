package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Unit tests for the class.
 */
@SpringBootTest
@ContextConfiguration
class CourseUnitTests {

  /**
   * The test course instance used for testing.
   */
  static Course testCourse;

  @BeforeAll
  static void setupCourseForTesting() {
    testCourse = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
  }

  @Test
  void toStringTest() {
    String expectedResult = "\nInstructor: Griffin Newbold; Location: 417 IAB; Time: 11:40-12:55";
    assertEquals(expectedResult, testCourse.toString());
  }

  @Test
  void enrollStudentTest() {
    boolean result = testCourse.enrollStudent();
    assertFalse(result);
  }

  @Test
  void enrollStudentWhenFullTest() {
    Course course = new Course("Griffin Newbol", "417 IAB", "11:40-12:55", 1);
    course.enrollStudent();
    boolean result = course.enrollStudent();
    assertFalse(result);
  }

  @Test
  void dropStudentTest() {
    testCourse.enrollStudent();
    boolean result = testCourse.dropStudent();
    assertFalse(result);
  }

  @Test
  void dropStudentWhenZeroEnrollmentTest() {
    Course course = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
    course.dropStudent();
    assertEquals(500 - 1, course.getEnrolledStudentCount());
  }

  @Test
  void reassignInstructorTest() {
    Course course = new Course("Griffin Newbol", "417 IAB", "11:40-12:55", 250);
    course.reassignInstructor("Adam Cannon");
    assertEquals("Adam Cannon", course.getInstructorName());
  }

  @Test
  void reassignLocationTest() {
    Course course = new Course("Griffin Newbol", "417 IAB", "11:40-12:55", 250);
    course.reassignLocation("309 HAV");
    assertEquals("309 HAV", course.getCourseLocation());
  }

  @Test
  void reassignTimeTest() {
    Course course = new Course("Griffin Newbol", "417 IAB", "11:40-12:55", 250);
    course.reassignTime("4:10-5:25");
    assertEquals("4:10-5:25", course.getCourseTimeSlot());
  }

  @Test
  void isCourseFullTest() {
    Course course = new Course("Griffin Newbol", "417 IAB", "11:40-12:55", 1);
    course.enrollStudent();
    course.enrollStudent();
    assertFalse(course.isCourseFull());
  }

  @Test
  void isCourseNotFullTest() {
    testCourse.enrollStudent();
    assertFalse(testCourse.isCourseFull());
  }

  @Test
  void setEnrolledStudentCountTest() {
    Course course = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
    course.setEnrolledStudentCount(100);
    assertEquals(100, course.getEnrolledStudentCount());
  }

  @Test
  void constructorTest() {
    Course course = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
    assertEquals("Griffin Newbold", course.getInstructorName());
  }


}

