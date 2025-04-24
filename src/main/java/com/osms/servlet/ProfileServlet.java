package com.osms.servlet;

import com.osms.model.User;
import com.osms.model.UserProfile;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet("/profile/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 5,   // 5 MB
    maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class ProfileServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            UserProfile profile = new UserProfile();
            UserProfile userProfile = profile.getProfileByUserId(user.getUserId());
            request.setAttribute("profile", userProfile);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load profile");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getPathInfo();
        if (action == null) action = "/update";

        try {
            switch (action) {
                case "/update":
                    updateProfile(request, response, user);
                    break;
                case "/upload-image":
                    uploadProfileImage(request, response, user);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update profile");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response, User user) 
            throws SQLException, ServletException, IOException {
        UserProfile profile = new UserProfile();
        profile.setUserId(user.getUserId());
        profile.setFirstName(request.getParameter("firstName"));
        profile.setLastName(request.getParameter("lastName"));
        profile.setEmail(request.getParameter("email"));
        profile.setPhone(request.getParameter("phone"));
        profile.setAddress(request.getParameter("address"));
        profile.setActive(true);

        if (profile.updateProfile()) {
            request.setAttribute("success", "Profile updated successfully");
        } else {
            request.setAttribute("error", "Failed to update profile");
        }
        
        request.setAttribute("profile", profile);
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    private void uploadProfileImage(HttpServletRequest request, HttpServletResponse response, User user) 
            throws SQLException, ServletException, IOException {
        Part filePart = request.getPart("profileImage");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        
        // Generate unique filename
        String uniqueFileName = user.getUserId() + "_" + System.currentTimeMillis() + "_" + fileName;
        String uploadPath = getServletContext().getRealPath("/uploads/profiles/");
        Path uploadDir = Path.of(uploadPath);
        
        // Create directory if it doesn't exist
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Save file
        filePart.write(uploadPath + uniqueFileName);

        // Update profile with new image path
        UserProfile profile = new UserProfile();
        profile.setUserId(user.getUserId());
        profile.setProfileImage("uploads/profiles/" + uniqueFileName);
        
        if (profile.updateProfile()) {
            request.setAttribute("success", "Profile image updated successfully");
        } else {
            request.setAttribute("error", "Failed to update profile image");
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }
} 