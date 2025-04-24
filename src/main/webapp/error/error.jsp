<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - OSMS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: #f8f9fa;
        }
        .error-container {
            text-align: center;
            padding: 2rem;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
            max-width: 500px;
            width: 90%;
        }
        .error-code {
            font-size: 6rem;
            font-weight: bold;
            color: #dc3545;
            margin-bottom: 1rem;
        }
        .countdown {
            font-size: 2rem;
            color: #0d6efd;
            margin: 1rem 0;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-code">Error</div>
        <h2>Something went wrong</h2>
        <p class="text-muted">An unexpected error occurred while processing your request.</p>
        <div class="countdown" id="countdown">5</div>
        <p>Redirecting to home page in <span id="timer">5</span> seconds...</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go Home Now</a>
        
        <% if (exception != null) { %>
            <div class="mt-4">
                <p class="text-danger">Error details (visible in development mode only):</p>
                <pre class="text-start bg-light p-3 small"><%= exception.getMessage() %></pre>
            </div>
        <% } %>
    </div>

    <script>
        let timeLeft = 5;
        const timerDisplay = document.getElementById('timer');
        const countdownDisplay = document.getElementById('countdown');

        const countdown = setInterval(() => {
            timeLeft--;
            timerDisplay.textContent = timeLeft;
            countdownDisplay.textContent = timeLeft;
            
            if (timeLeft <= 0) {
                clearInterval(countdown);
                window.location.href = '${pageContext.request.contextPath}/';
            }
        }, 1000);
    </script>
</body>
</html> 