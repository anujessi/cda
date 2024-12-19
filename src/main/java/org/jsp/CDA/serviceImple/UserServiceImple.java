package org.jsp.CDA.serviceImple;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.jsp.CDA.DTO.Authantication;
import org.jsp.CDA.Dao.AdministerDao;
import org.jsp.CDA.Dao.FacultyDao;
import org.jsp.CDA.Dao.StudentDao;
import org.jsp.CDA.Dao.UserDao;
import org.jsp.CDA.ResponseStrcture.ResponseStrcture;
import org.jsp.CDA.entity.Administrator;
import org.jsp.CDA.entity.Faculty;
import org.jsp.CDA.entity.Student;
import org.jsp.CDA.entity.User;
import org.jsp.CDA.exceptionClass.InvalidCredentialsException;
import org.jsp.CDA.exceptionClass.InvalidUserIdException;
import org.jsp.CDA.exceptionClass.NoUsersFoundException;
import org.jsp.CDA.service.UserService;
import org.jsp.CDA.util.MyUtil;
import org.jsp.CDA.util.Role;
import org.jsp.CDA.util.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImple implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired 
	private JavaMailSender jms;
	

	@Override
	public ResponseEntity<?> saveUser(User user) {

		user = userDao.saveUser(user);
		
		user.setOtp(MyUtil.getOtp());
		MimeMessage mimeMessage= jms.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper= new MimeMessageHelper(mimeMessage,true);
			mimeMessageHelper.addTo(user.getEmail());
			mimeMessageHelper.setSubject("Account Created");
			mimeMessageHelper.setText("Hello! "+user.getName()+ user. getOtp()+" "+" your account has been created ");
			jms.send(mimeMessage);
		
		} catch (MessagingException e) {
			e.printStackTrace();
		}

//		if (user.getRole() == Role.ADMINISTER)
//			adminDao.saveAdminister(Administrator.builder().id(user.getId()).user(user).build());
//		else if (user.getRole() == Role.FACULTY)
//			facDao.saveFaculty(Faculty.builder().id(user.getId()).user(user).build());
//		else
//		stuDao.saveStudent(Student.builder().id(user.getId()).user(user).build());
		
		
		
		
		return ResponseEntity.status(HttpStatus.OK.value()).body(ResponseStrcture.builder()
				.httpStatus(HttpStatus.OK.value()).message("user saved  successfully").body(user).build());
	}
	
	

	@Override
	public ResponseEntity<?> findAllUsers() {

		List<User> list = userDao.findAllUsers();

		if (list.isEmpty()) {

			throw NoUsersFoundException.builder().message("no users found").build();
		}
		return ResponseEntity.status(HttpStatus.OK.value()).body(ResponseStrcture.builder()
				.httpStatus(HttpStatus.OK.value()).message("found all users successfully").body(list).build());
	}

	@Override
	public ResponseEntity<?> findUserById(int id) {
		Optional<User> user = userDao.findUserById(id);

		if (user.isEmpty()) {

			throw InvalidUserIdException.builder().message("no users found").build();
		}
		return ResponseEntity.status(HttpStatus.OK.value()).body(ResponseStrcture.builder()
				.httpStatus(HttpStatus.OK.value()).message("find user successfully").body(user.get()).build());
	}
	
	@Override
	public ResponseEntity<?> findUserByEmail(String email) {
		
		Optional <User> user=userDao.findUserByEmail(email);
		if (user.isEmpty()) {
		throw InvalidUserIdException.builder().message("no users found").build();
	}
	return ResponseEntity.status(HttpStatus.OK.value()).body(ResponseStrcture.builder()
			.httpStatus(HttpStatus.OK.value()).message("find user successfully").body(user.get()).build());

		
			
		}



	@Override
	public ResponseEntity<?> login(Authantication auth) {

		Optional<User> optional = userDao.login(auth.getUsername(), auth.getPassword());

		if (optional.isEmpty()) {
			throw InvalidCredentialsException.builder().message("invalid credentials").build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(ResponseStrcture.builder().httpStatus(HttpStatus.OK.value())
				.message("login  successfully").body(optional.get()).build());
	}

	@Override
	public String deletUserById(int id) {
		 Optional<User> u = userDao.findUserById(id);
		 if(u.isEmpty()) {
			 
		 }
		 User user = u.get();
		 userDao.deleteUserById(id);
		return "deleted successfully";
	}



	@Override
	public ResponseEntity<?> verifyOtp(int id, int otp) {
		Optional<User>optional=userDao.findUserById(id);
		
		if(optional.isEmpty()) {
			throw new RuntimeException("invalid  user id unable to verify the otp");
		}
			User user=optional.get();
					if(otp!=user.getOtp()) {
						throw new RuntimeException(" inavlid otp unable to find the otp");
			
		}
					
					user.setStatus(UserStatus.ACTIVE);
					user=userDao.saveUser(user);
					return ResponseEntity.status(HttpStatus.OK).body(ResponseStrcture.builder().httpStatus(HttpStatus.OK.value())
							.message("login  successfully").body(optional.get()).build());
	}



	
	
	

}
