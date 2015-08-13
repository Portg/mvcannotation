package struts.example;

import org.jeecg.learning.chapter3.annotation.Action;
import org.jeecg.learning.chapter3.annotation.RequestMapping;
import org.jeecg.learning.chapter3.annotation.RequestMethod;
import org.jeecg.learning.chapter3.annotation.Result;

@Action("exampleAction")
public class ExampleAction {

	@RequestMapping(value = "/example.do", method = RequestMethod.POST
			, results = {
					@Result(name="success", location = "/WEB-INF/view/exampleSucc.jsp"),
					@Result(name="failure", location = "/WEB-INF/view/exampleFail.jsp")})
	public String execute(ExampleForm form) {

		String page = "failure";
		if (form.getUserName().equals("admin")
				&& form.getPassWord().equals("pass")
				&& form.getRandCode().equals("1234")) {
			page= "success";
		}
		return page;
	}

}
