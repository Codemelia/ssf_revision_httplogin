flow of app:

1. Login page
    - Successful login -> Secret Page
    - 1st unsuccessful login -> Login Page
    - 2nd unsuccessful login -> Captcha Page

2. Captcha page
    - Successful login -> Secret Page
    - 3rd unsuccessful login -> Account Locked Page

3. If user tries to access Secret/Captcha page without authorisation, redirect to login page