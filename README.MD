# Notes
## _Final Project in GoIT School Java Dev4_


Final project of group No.1 on creating the Notes Spreeng Boot application.

## Team

- Ihor Beztsennyi - team lead;
- Sergey Kobzar - developer;
- Serhii Poltavets - developer;
- Tetiana Revenok - developer;
- Vitalii Karakai - developer.

## Features

- Spring Security implementation;
- Create new accaunt with Google Captcha;
- View all public user's notes;
- CRUD operations on notes;
- Notes sharing with other users;
- About us page;
- Available for admin hidden CRUD operations on users.


## Environment Variables

Environment variables for PostgreSQL:

- spring.datasource.driver-class-name=${DB_DRIVER_CLASS_NAME:org.postgresql.Driver}
- spring.datasource.password=${DB_PASSWORD:6be5f302abd6c038bb90b56996d4b4cba1a39df95e3101d758f8059476076ba8}
- spring.datasource.url=${DB_URL:jdbc:postgresql://ec2-176-34-215-248.eu-west-1.compute.amazonaws.com:5432/d6lhr70e6glnfj}
