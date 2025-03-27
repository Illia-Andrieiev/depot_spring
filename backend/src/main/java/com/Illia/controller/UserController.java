@WebServlet("/users")
public class UserController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<UserDTO> users = userService.getAllUsers();
        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(users));
    }
}
