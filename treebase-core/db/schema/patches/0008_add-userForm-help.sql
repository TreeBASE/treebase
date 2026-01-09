insert into versionhistory(patchnumber, patchlabel, patchdescription) 
       values (8, 'add-userForm-help', 
       'Add help message for user registration form'); 

INSERT INTO help (help_id, version, tag, helptext)
VALUES (
    nextval('help_id_sequence'),
    1,
    'userForm',
    '<h2>User Registration</h2>' ||
    '<p>To register for a TreeBASE account, please provide the following required information:</p>' ||
    '<ul>' ||
    '<li><strong>Username</strong>: Choose a unique username for your account.</li>' ||
    '<li><strong>Password</strong>: Enter a secure password.</li>' ||
    '<li><strong>Re-type Password</strong>: Re-enter your password to confirm it and avoid mistyping.</li>' ||
    '<li><strong>Email Address</strong>: Provide a valid email address for account communication.</li>' ||
    '</ul>' ||
    '<p>The name and phone number fields are optional.</p>'
);

