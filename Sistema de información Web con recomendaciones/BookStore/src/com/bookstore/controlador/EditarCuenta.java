package com.bookstore.controlador;

import com.bookstore.modelo.VO.UsuarioVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import static com.bookstore.modelo.TiendaFacade.actualizarUsuarioFacade;
import static com.bookstore.modelo.TiendaFacade.existeEmailFacade;
import static com.bookstore.modelo.TiendaFacade.leerUsuarioFacade;

/**
 * Servlet implementation class CrearUsuarioServlet
 */
public class EditarCuenta extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public EditarCuenta() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());

		boolean cambioUser = false;
		boolean cambioMail = false;
		boolean cambioPass = false;

		boolean cambiosRealizados = false;

		boolean errores = false;
		HashMap tablaErrores = new HashMap();
		System.out.println("He llegado aquí con el usuario logi");
		String errorNombre = "";
		String username = request.getParameter(CommonConstants.usernameParameterName);
		UsuarioVO user = null;
		try {
			user = leerUsuarioFacade(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("----------------------------NOMBRE:"+username+":");


		//-----------------------------
		String errorMail = "";
		String mail = request.getParameter(CommonConstants.emailProfileParameterName);
		if(mail != null) {
			if(!user.getEmail().equals(mail)) {
				if (mail.trim().equals(new String(""))) {
					errorMail = "El email no puede ser vacio";
					tablaErrores.put(CommonConstants.emailProfileParameterName, errorMail);
					errores = true;
				} else if (existeEmailFacade(mail)) {
					errorMail = "El email ya esta en uso";
					tablaErrores.put(CommonConstants.emailProfileParameterName, errorMail);
					errores = true;
				} else {
					cambioMail = true;
				}
			}
		}
		//-----------------------------
		String errorPassword = "";
		String password1 = request.getParameter(CommonConstants.password1ParameterName);
		String password2 = request.getParameter(CommonConstants.password2ParameterName);
		if(password1 != null && password2!=null) {
			if (!password1.trim().equals(new String("")) || !password2.trim().equals(new String(""))) {
				if(!password1.trim().equals(password2.trim())) {
					errorPassword = "Las contraseñas no coinciden";
					tablaErrores.put("password", errorPassword);
					errores = true;
				}else{
					if(password1.trim().length() < 5){
						errorPassword = "Las contraseñas deben tener 5 caracteres como mínimo";
						tablaErrores.put(CommonConstants.passwordParameterName, errorPassword);
						errores = true;
					}
				}
				cambioPass = true;
			}else{
				cambioPass = true;
			}
		}
		//-----------------------------
		System.out.println("Despues de leer parametros");
		if (errores==false){
			try{
				System.out.println("Voy a llamar a la fachada y métdo Actualziar Usuario de ");
				if(cambioUser){user.setNombreDeUsuario(username); cambiosRealizados=true;}
				if(cambioMail){user.setEmail(mail); cambiosRealizados = true;}
				if(cambioPass){user.setEncryptedPassword(password1); cambiosRealizados = true;}
				actualizarUsuarioFacade (user);
				System.out.println("->error antes " + cambiosRealizados);
				request.setAttribute(CommonConstants.succesParameterName, cambiosRealizados);
				RequestDispatcher dispacher = request.getRequestDispatcher("EditarCuenta.jsp");
				dispacher.forward(request, response);
			}catch (Exception e){
				e.printStackTrace(System.err);
				System.out.println("Problemas con el VN");
				response.sendRedirect("errorInterno.html");
			}

		}else{
			request.setAttribute("errores", tablaErrores);
			RequestDispatcher dispacher = request.getRequestDispatcher("EditarCuenta.jsp");
			System.out.println("Antes de hacer el forward los valores....");
			dispacher.forward(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}