<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Email Confirmation</title>
    <style>
        body {
            font-family: 'Helvetica Neue', Arial, sans-serif;
            line-height: 1.5;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 600px;
            margin: 30px auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        .header {
            background-color: #007BFF; /* Bootstrap Primary Color */
            color: white;
            padding: 20px;
            text-align: center;
        }
        .header h1 {
            margin: 0;
            font-size: 24px;
            font-weight: 600;
        }
        .content {
            padding: 20px;
        }
        .content h2 {
            font-size: 20px;
            margin-bottom: 15px;
            color: #333;
        }
        .content p {
            margin: 10px 0;
            color: #555;
        }
        .button {
            display: inline-block;
            padding: 12px 25px;
            margin: 20px 0;
            font-size: 16px;
            color: white;
            background-color: #007BFF; /* Bootstrap Primary Color */
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        .button:hover {
            background-color: #0056b3; /* Darker shade for hover */
        }
        .footer {
            padding: 20px;
            text-align: center;
            background-color: #f4f4f4;
            border-top: 1px solid #ddd;
            font-size: 14px;
            color: #777;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Email Confirmation</h1>
    </div>
    <div class="content">
        <h2>Hello, ${name}!</h2>
        <p>Thank you for registering. To complete the process and confirm your account, please click on the button below:</p>
        <a href="${link}" class="button">Confirm Registration</a>
        <p>If you did not register on our site, please disregard this email.</p>
    </div>
    <div class="footer">
        <p>© 2024 Your Company. All rights reserved.</p>
    </div>
</div>
</body>
</html>
