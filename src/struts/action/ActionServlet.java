package struts.action;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import struts.example.ExampleAction;
import struts.example.ExampleForm;

public class ActionServlet extends HttpServlet {

	private static final long serialVersionUID = -3137831652369054389L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String servletPath = req.getServletPath();
		@SuppressWarnings("unchecked")
		Map<String, ActionMapping> map = (Map<String, ActionMapping>)req
				.getServletContext().getAttribute("actionMap");
		ActionMapping actionMapping = map.get(servletPath);
		String formType = actionMapping.getFormType();

		ExampleForm form = (ExampleForm) this.fillExampleForm(req, formType);

		String actionType = actionMapping.getType();

		ExampleAction action = (ExampleAction) this.getActionObject(actionType);
		String url = action.execute(form);
		String result = actionMapping.getForwardProperties().get(url);

		RequestDispatcher requestDispatcher = req.getRequestDispatcher(result);
		requestDispatcher.forward(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private Object getActionObject(String actionType) {
		Class<?> actionClazz;
		Object action = null;
		try {
			actionClazz = Class.forName(actionType);
			action      = actionClazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return action;
	}

	private Object fillExampleForm(HttpServletRequest request, String formType) {
		Object actionForm = null;
		try {
			Class<?> formClazz = Class.forName(formType);
			actionForm         = formClazz.newInstance();
			Field[] fields     = formClazz.getDeclaredFields();
			for(Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				field.set(actionForm, request.getParameter(fieldName));
				field.setAccessible(false);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actionForm;
	}
}
