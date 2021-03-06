package in.ajinkyadhote.lms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.ajinkyadhote.lms.model.Book;
import in.ajinkyadhote.lms.model.Issuedbook;
import in.ajinkyadhote.lms.service.BookService;
import in.ajinkyadhote.lms.service.IssuedbookService;

@RestController
@RequestMapping("/books")
public class BookController {
	
	@Autowired
	BookService bookService;
	
	@Autowired
	IssuedbookService  issuedbookService;

	@RequestMapping("/all")
	public Object getAllStudents(){
		return bookService.findAll();
	}
	
	@RequestMapping("{id}")
	public Book getStudent(@PathVariable("id") Long id) {
		return bookService.findById(id);
	}
	
	 @RequestMapping(value = "/create", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)	  
	  @ResponseBody
	  //public String create(@RequestParam(value = "id", required = true) Long id, @RequestParam("name")String name,@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("PhoneNo") String PhoneNo) {
	  public String create(@RequestBody Book Book) {
		  String userId = "";
	    try {	    
	      //Book user = new Book();
	    	bookService.save(Book);
	     // userId = String.valueOf(user.getId());
	      userId = String.valueOf(Book.getId());
	    }
	    catch (Exception ex) {
	      return "Error creating the user: " + ex.toString();
	    }
	    return "User succesfully created with id = " + userId;
	  }
	 
	 @RequestMapping("/update/{id}")
	  @ResponseBody
	  //public String updateUser(@PathVariable("id") Long id, @PathVariable("name")String name,@PathVariable("email") String email,@PathVariable("password") String password,@PathVariable("PhoneNo") String PhoneNo) {
	  public Map<String, Object> updateUser(@PathVariable Long id) {
		 Map<String, Object> result = new HashMap<String, Object>();
		 try {
	    	//Person user = personService.findById(id);
			Book book = bookService.findById(id);
			//book.setId(id);
			book.setAvailable(book.getAvailable()+1);
	    	bookService.save(book);
	    	
	    	Date startDate = new Date();
	    	Date endDate = new Date(startDate.getTime() + (1000*60*12*7));
	    	Issuedbook  issuedbook = new Issuedbook();
	    	issuedbook.setBookid(book.getId());
	    	issuedbook.setStartdate(startDate);
	    	issuedbook.setEnddate(endDate);
	    	issuedbook.setStudent((long) 1);
	    	
	    	issuedbookService.save(issuedbook);
	    	
	    }
	    catch (Exception ex) {
	    	System.out.println(ex);
	    	ex.printStackTrace();
	      result.put("result","Error requesting book: " + ex.toString());
	      return result;
	    }
	   result.put("result","Book succesfully requested!");
	   return result;
	  }
}
