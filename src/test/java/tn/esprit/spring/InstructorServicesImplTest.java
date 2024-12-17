package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import org.junit.jupiter.api.extension.ExtendWith;
import tn.esprit.spring.services.InstructorServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InstructorServicesImplTest {

	@Mock
	private IInstructorRepository instructorRepository;

	@Mock
	private ICourseRepository courseRepository;

	@InjectMocks
	private InstructorServicesImpl instructorServices;

	private Instructor instructor;
	private Course course;

	@BeforeEach
	public void setUp() {
		// Setup Instructor
		instructor = new Instructor();
		instructor.setNumInstructor(1L);
		instructor.setFirstName("John");
		instructor.setLastName("Doe");
		instructor.setDateOfHire(LocalDate.now());

		// Setup Course
		course = new Course();
		course.setNumCourse(1L);
		course.setLevel(1);
		course.setTypeCourse(TypeCourse.INDIVIDUAL);  // Assuming you have an Enum for TypeCourse
		course.setSupport(Support.SNOWBOARD);        // Assuming you have an Enum for Support
		course.setPrice(200.0f);
		course.setTimeSlot(2);
	}

	@Test
	public void testAddInstructor() {
		// Mock the repository save method
		when(instructorRepository.save(Mockito.any(Instructor.class))).thenReturn(instructor);

		// Execute the service method
		Instructor savedInstructor = instructorServices.addInstructor(instructor);

		// Assertions
		assertNotNull(savedInstructor);
		assertEquals("John", savedInstructor.getFirstName());
		assertEquals("Doe", savedInstructor.getLastName());
	}

	@Test
	public void testRetrieveInstructor() {
		// Mock the repository findById method
		when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

		// Execute the service method
		Instructor retrievedInstructor = instructorServices.retrieveInstructor(1L);

		// Assertions
		assertNotNull(retrievedInstructor);
		assertEquals("John", retrievedInstructor.getFirstName());
		assertEquals("Doe", retrievedInstructor.getLastName());
	}

	@Test
	public void testUpdateInstructor() {
		// Mock the repository save method
		when(instructorRepository.save(Mockito.any(Instructor.class))).thenReturn(instructor);

		// Execute the service method
		Instructor updatedInstructor = instructorServices.updateInstructor(instructor);

		// Assertions
		assertNotNull(updatedInstructor);
		assertEquals("John", updatedInstructor.getFirstName());
		assertEquals("Doe", updatedInstructor.getLastName());
	}

	@Test
	public void testAddInstructorAndAssignToCourse() {
		// Mock the repository findById methods for both instructor and course
		when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
		when(instructorRepository.save(Mockito.any(Instructor.class))).thenReturn(instructor);

		// Execute the service method
		Instructor savedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

		// Assertions
		assertNotNull(savedInstructor);
		assertNotNull(savedInstructor.getCourses());

		Set<Course> assignedCourses = savedInstructor.getCourses();
		assertEquals(1, assignedCourses.size());
		assertTrue(assignedCourses.contains(course));
	}

	@Test
	public void testRetrieveAllInstructors() {
		// Mocking repository's findAll method
		when(instructorRepository.findAll()).thenReturn(Arrays.asList(instructor));

		// Execute the service method
		List<Instructor> instructors = instructorServices.retrieveAllInstructors();

		// Assertions
		assertNotNull(instructors);
		assertEquals(1, instructors.size());
		assertEquals("John", instructors.get(0).getFirstName());
	}

}
