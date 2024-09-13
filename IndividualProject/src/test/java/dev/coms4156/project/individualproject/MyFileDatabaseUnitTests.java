package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Unit tests for the class.
 */
@SpringBootTest
@ContextConfiguration
class MyFileDatabaseUnitTests {

  MyFileDatabase database;
  File tempFile;

  @BeforeEach
  void setUp() throws IOException {
    tempFile = File.createTempFile("testData", ".txt");
    database = new MyFileDatabase(1, tempFile.getAbsolutePath());
  }

  @AfterEach
  void tearDown() throws IOException {
    if (tempFile.exists()) {
      tempFile.delete();
    }
  }

  @Test
  void emptyDatabaseTest() {
    Map<String, Department> emptyMapping = new HashMap<>();
    database.setMapping(emptyMapping);
    assertTrue(database.getDepartmentMapping().isEmpty());
  }

  @Test
  void setMappingTest() {
    Map<String, Department> departmentMapping = new HashMap<>();
    Department comsDepartment = new Department("COMS", new HashMap<>(), "Griffin Newbold", 250);
    departmentMapping.put("coms", comsDepartment);

    database.setMapping(departmentMapping);
    assertEquals(departmentMapping, database.getDepartmentMapping());
  }

  @Test
  void saveContentsToFileTest() {
    Map<String, Department> departmentMapping = new HashMap<>();
    Department comsDepartment = new Department("COMS", new HashMap<>(), "Griffin Newbold", 10);
    departmentMapping.put("COMS", comsDepartment);

    database.setMapping(departmentMapping);
    database.saveContentsToFile();

    MyFileDatabase loadDatabase = new MyFileDatabase(0, tempFile.getAbsolutePath());
    Map<String, Department> loadMapping = loadDatabase.getDepartmentMapping();

    assertEquals(departmentMapping, loadMapping);
  }

  @Test
  void deserializeObjectFromFileWithValidDataTest() {
    Map<String, Department> departmentMapping = new HashMap<>();
    Department comsDepartment = new Department("COMS", new HashMap<>(), "Griffin Newbold", 10);
    departmentMapping.put("COMS", comsDepartment);
    database.setMapping(departmentMapping);
    database.saveContentsToFile();
    Map<String, Department> loadMapping = database.deSerializeObjectFromFile();
    assertEquals(departmentMapping, loadMapping);
  }

  @Test
  void deSerializeObjectFromFileWithInvalidDataTest() throws IOException {
    File emptyFile = new File(tempFile.getAbsolutePath());
    emptyFile.delete();
    emptyFile.createNewFile();

    MyFileDatabase loadDatabase = new MyFileDatabase(0, tempFile.getAbsolutePath());
    Map<String, Department> loadMapping = loadDatabase.deSerializeObjectFromFile();

    assertNull(loadMapping);
  }

  @Test
  void multipleSaveLoadTest() {
    Map<String, Department> departmentMapping = new HashMap<>();
    Department comsDepartment = new Department("COMS", new HashMap<>(), "Griffin Newbold", 10);
    departmentMapping.put("COMS", comsDepartment);

    database.setMapping(departmentMapping);
    database.saveContentsToFile();


    comsDepartment.addPersonToMajor();
    database.setMapping(departmentMapping);
    database.saveContentsToFile();

    MyFileDatabase loadDatabase2 = new MyFileDatabase(0, tempFile.getAbsolutePath());
    Map<String, Department> loadMapping2 = loadDatabase2.getDepartmentMapping();
    assertEquals(departmentMapping, loadMapping2);
  }

  @Test
  void saveMultipleDepartmentsTest() {
    Map<String, Department> departmentMapping = new HashMap<>();
    Department comsDepartment = new Department("COMS", new HashMap<>(), "Griffin Newbold", 10);
    Department ieorDepartment = new Department("IEOR", new HashMap<>(), "Jay Sethuraman", 12);
    departmentMapping.put("COMS", comsDepartment);
    departmentMapping.put("IEOR", ieorDepartment);

    database.setMapping(departmentMapping);
    database.saveContentsToFile();

    MyFileDatabase loadDatabase = new MyFileDatabase(0, tempFile.getAbsolutePath());
    Map<String, Department> loadMapping = loadDatabase.getDepartmentMapping();

    assertEquals(2, loadMapping.size());
  }

  @Test
  void toStringTest() {
    Map<String, Department> departmentMapping = new HashMap<>();
    Department comsDepartment = new Department("COMS", new HashMap<>(), "Griffin Newbold", 10);
    departmentMapping.put("COMS", comsDepartment);

    database.setMapping(departmentMapping);

    String str = "For the COMS department: \n" + comsDepartment;
    assertEquals(str, database.toString());
  }


}