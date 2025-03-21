# Shipment App

Study Case to learn Java on Backend with Spring Boot.

# App Design

This app focus on Back-End, so it's not quite responsive

| Page | Index | Create | Read | Update |
| -    | -     | -      | -    | -      |
| Shipments | ![image](https://github.com/user-attachments/assets/d702ec24-7269-4cdb-8630-0f2d66a90c51) | ![image](https://github.com/user-attachments/assets/941bd974-9cd0-41bd-984c-41737dfb96e2) | ![image](https://github.com/user-attachments/assets/64873947-ed35-48a9-b47a-f2c400d8c1ce) | ![image](https://github.com/user-attachments/assets/d7106d28-b2e5-42e3-bf82-a69c17e7c262) | 
| Products  | ![image](https://github.com/user-attachments/assets/f403ca58-fe72-485a-a9c2-f948d7fb4dd8) | ![image](https://github.com/user-attachments/assets/be6fedf8-6262-4749-b8b3-06c036d21bc5) | ![image](https://github.com/user-attachments/assets/4ccb6b0c-3f0b-4fdf-a35c-dc5e0f3a001c) | ![image](https://github.com/user-attachments/assets/db1597cb-f3f2-4545-98fc-199e5df358e2) |
| Merchant  | ![image](https://github.com/user-attachments/assets/91f5517a-8079-489d-a039-04576d5b168a) | ![image](https://github.com/user-attachments/assets/f43cb9c8-d460-477e-8ac0-ef6d844a279a) | ![image](https://github.com/user-attachments/assets/9c85fef9-0d89-44ca-b22e-fcbf36175873) | ![image](https://github.com/user-attachments/assets/3c875aed-907d-4019-98b6-59901349d000) |
| Customer  | ![image](https://github.com/user-attachments/assets/9df9816f-d441-48a8-838a-e2b3910a9a08) | ![image](https://github.com/user-attachments/assets/79ce9d99-0192-446c-b4eb-b35f59b197a4) | ![image](https://github.com/user-attachments/assets/a8d66ac9-6df5-43f7-a94b-a00528887c36) | ![image](https://github.com/user-attachments/assets/11da96ea-ec17-4c41-9229-6fa53dd1925d) |

## How to Do

- Using Spring Initialzr, with these dependencies
  - Spring Web
  - Spring Boot DevTools
  - MySQL Driver
  - Spring Data JPA
  - Validation
  - Thymeleaf

- Make Static Index (Main Landing)

- Setting Application Properties
  - DB Connection
  - File Size Config for Image

- Make Database (using MySQL and SQLyog)

- Package Model
  - variable for each column in database's table
  - id for related Model
  - Getter() and Setter()
  - Model Data Transfer Object, used to transfer data to model on create or update

- Package Repo (JPA)
-- Can add additional function, like getByModel and deleteByModel

- Package Controller
-- CRUD functions

## To Do Next

What I think I can add to learn more
- Using Login and Register
- Make some kind of middleware for different user roles
- Turn this app to an API
