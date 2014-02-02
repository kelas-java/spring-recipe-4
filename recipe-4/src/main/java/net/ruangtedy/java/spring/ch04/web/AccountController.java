package net.ruangtedy.java.spring.ch04.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/users")
public class AccountController {
	private static final String VN_REG_FORM="users/registrationForm";
	private static final String VN_REG_OK="redirect:users/registration_ok.html";
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		binder.setAllowedFields(new String[]{
				"username", "password", "confirmPassword",
				"firstName", "lastName", "email", "marketingOk","acceptTerms"
		});
		
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		
	}
	
	@RequestMapping(value="new", method=RequestMethod.GET)
	public String getRegistrationForm(Model model){
		model.addAttribute("account", new AccountForm());
		return VN_REG_FORM;
		
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String postRegistrationForm(@ModelAttribute("account") @Valid AccountForm form,
			BindingResult result){
		convertPasswordError(result);
		//log.info("created Registration: {}", form);
		return (result.hasErrors()? VN_REG_FORM:VN_REG_OK);
		
	}
	
	private static void convertPasswordError(BindingResult result) {
		// Map class-level Hibernate error message to field-level Spring error message.
		for (ObjectError error : result.getGlobalErrors()) {
			String msg = error.getDefaultMessage();
			if ("account.password.mismatch.message".equals(msg)) {
				// Don't show if there's already some other error message.
				if (!result.hasFieldErrors("password")) {
					result.rejectValue("password", "error.mismatch");
				}
			}
		}
	}
	
}
