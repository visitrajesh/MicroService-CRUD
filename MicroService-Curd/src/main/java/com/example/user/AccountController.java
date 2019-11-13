package com.example.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class AccountController {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	 @GetMapping("/users/{userId}/accounts")
	    public List < Account > getAccountsByUser( @PathVariable(value = "userId") Long userId) {
	        return accountRepository.findByUserId(userId);
	    }
	 
	 @PostMapping("/users/{userId}/accounts")
	    public ResponseEntity<Object> createAccount(@PathVariable(value = "userId") Long userId,
	        @Valid @RequestBody Account account) throws UserNotFoundException {
		 
		 if (!userRepository.existsById(userId)) {
	            throw new UserNotFoundException("User not found");
	        }
		 User user=new User();
		 user.setId(userId);
		 account.setUser(user);
		 
			Account savedAccount = accountRepository.save(account);

			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(savedAccount.getId()).toUri();

			return ResponseEntity.created(location).build();
	        
	    }

	 @DeleteMapping("/users/{userId}/accounts/{accountId}")
	 public void deleteAccounts(@PathVariable(value = "userId") Long userId,
		        @PathVariable(value = "accountId") Long accountId) throws AccountNotFoundException {
		 
		 if (!userRepository.existsById(userId)) {
	            throw new UserNotFoundException("User not found");
	        }
		 if (!accountRepository.existsById(accountId)) {
	            throw new AccountNotFoundException("User not found");
	        }
			accountRepository.deleteById(accountId);
	 
		}

	 @PutMapping("/users/{userId}/accounts/{accountId}")
	    public ResponseEntity<Object> updateCourse(@PathVariable(value = "instructorId") Long userId,
	        @PathVariable(value = "courseId") Long accountId, @Valid @RequestBody Account accountRequest) throws AccountNotFoundException {
		 
	        if (!userRepository.existsById(userId)) {
	            throw new UserNotFoundException("User not found");
	        }
	        accountRequest.setId(accountId);
	        accountRepository.save(accountRequest);
			return ResponseEntity.noContent().build();
	    }
}
