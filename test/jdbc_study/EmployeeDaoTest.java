package jdbc_study;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import jdbc_study.dao.EmployeeDao;
import jdbc_study.daoimpl.EmployeeDaoImpl;
import jdbc_study.dto.Department;
import jdbc_study.dto.Employee;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeeDaoTest {
	static final Logger log = LogManager.getLogger();
	static EmployeeDao dao;
	static File picsDir;
	static File imagesDir;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		log.trace("setUpBeforeClass()");
		dao = new EmployeeDaoImpl();
		
		picsDir = new File(System.getProperty("user.dir")+ 
				           System.getProperty("file.separator") + 
				           "pics" + 
				           System.getProperty("file.separator"));
		
		if (!picsDir.exists()) {
			picsDir.mkdir();
		}

		imagesDir = new File(System.getProperty("user.dir")+ 
				           System.getProperty("file.separator") + 
				           "images" + 
				           System.getProperty("file.separator"));
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		log.trace("tearDownAfterClass()");
		dao = null;
/*
		if (picsDir.exists()) {
			for(File f : picsDir.listFiles()) {
				f.delete();
			}
			picsDir.delete();
		}
*/
	}

	@Test
	public void test01SelectEmployeeByAll() throws SQLException, FileNotFoundException, IOException {
		log.trace("testSelectEmployeeByAll()");
		List<Employee> list = dao.selectEmployeeByAll();
		Assert.assertNotNull(list);
		
		File imgFile = null;
		for(Employee e : list) {
			log.trace(e);
			if (e.getPic() != null) {
				imgFile = getPicFile(e);
				log.trace(imgFile.getAbsolutePath());
			}
		}
	}
	
	@Test
	public void test02InsertEmployee() throws SQLException {
		log.trace("test02InsertEmployee()");
		Employee newEmp = new Employee(1004, "이유영", "사원", new Employee(1003), 
				1500000, new Department(1), getImage("이유영.jpg"));
		
		int res = dao.insertEmployee(newEmp);
		log.trace("res = " + res);
		Assert.assertEquals(1, res);
	}

	@Test
	public void test03UpdateEmployee() throws SQLException {
		log.trace("test03UpdateEmployee()");
		Employee updateEmp = new Employee(1004, "청하", "인턴", new Employee(3426), 
				3500000, new Department(2));
		updateEmp.setPic(getImage("청하.jpg"));
		
		int res = dao.updateEmployee(updateEmp);
		Assert.assertEquals(1, res);
	}
	
	@Test
	public void test04SelectEmployeeByNo() throws FileNotFoundException, IOException, SQLException {
		log.trace("test04SelectEmployeeByNo()");
		Employee selEmp = dao.selectEmployeeByNo(new Employee(1004));
		Assert.assertNotNull(selEmp);
		log.trace(selEmp);
		if (selEmp.getPic() != null) {
			File imgFile = getPicFile(selEmp);
			log.trace(imgFile.getAbsolutePath());
		}
		
	}
	
	@Test
	public void test05DeleteEmployee() throws SQLException {
		log.trace("test05DeleteEmployee()");
		Employee delEmp = new Employee(1004);
		int res = dao.deleteEmployee(delEmp);
		Assert.assertEquals(1, res);
	}
	
	private byte[] getImage(String fileName) {
		byte[] pic = null;

		File imgFile = new File(imagesDir, fileName);
		
		try(InputStream is = new FileInputStream(imgFile);){
			pic = new byte[is.available()];
			is.read(pic);
		} catch (FileNotFoundException e) {
			System.out.println("해당 파일을 찾을 수 없음");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pic;
	}

	private File getPicFile(Employee e) throws FileNotFoundException, IOException {
		File file = null;
		file = new File(picsDir, e.getEmpName()+".jpg");
		try(FileOutputStream fos = new FileOutputStream(file)){
			fos.write(e.getPic());
		}
		return file;
	}

}












