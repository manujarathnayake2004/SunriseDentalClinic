# Software Requirements Traceability

## Functional requirements
| ID | Requirement | Implementation |
|---|---|---|
| FR-01 | Authorized staff login using username/password | Spring Security, BCrypt, `UserAccount`, login page |
| FR-02 | Register and maintain patient details | Patient controller/service/repository |
| FR-03 | Register appointment with unique number | Appointment controller/service + unique DB constraint |
| FR-04 | Store patient/address/contact/dentist/treatment/date/time | Relational entities and appointment form |
| FR-05 | Search using appointment number | Appointment repository search |
| FR-06 | Display complete patient and appointment details | Appointment details view |
| FR-07 | Prevent double/overlapping dentist bookings | Duration-aware service validation + DB trigger protection |
| FR-08 | Calculate treatment + consultation fee | Separate billing REST service + Strategy pattern |
| FR-09 | Print patient bill/receipt | Receipt view + browser print action |
| FR-10 | Provide step-by-step help | Help Guide page |
| FR-11 | Safely exit the system | Secure logout, session invalidation and JSESSIONID deletion |
| FR-12 | Provide decision-support reports | Appointment, workload, completion, revenue and daily summary reports |
| FR-13 | Export appointment report | CSV export; stored procedure used on MySQL/MariaDB with safe fallback |
| FR-14 | Alert staff to upcoming appointments | Dashboard 24-hour appointment reminder panel |
| FR-15 | Maintain dentist/treatment/staff master data | Admin-only management pages |
| FR-16 | Audit important system activity | Audit log including login success/failure/logout and data changes |
| FR-17 | Monitor distributed billing availability | Dashboard billing-service health indicator |

## Non-functional requirements
| ID | Requirement | Implementation |
|---|---|---|
| NFR-01 | Security | BCrypt, role authorization, CSRF, session cookie controls, no default credentials |
| NFR-02 | Usability | Responsive menu-driven UI, validation messages, recovery/error pages |
| NFR-03 | Maintainability | MVC, Service Layer, Repository and Strategy patterns |
| NFR-04 | Data integrity | Transactions, unique keys, overlap rules, one-bill-per-appointment |
| NFR-05 | Testability | JUnit, Mockito, MockMvc, H2 test profile, GitHub Actions, JaCoCo |
| NFR-06 | Auditability | Persistent audit log |
| NFR-07 | Distributed architecture | HTTP REST communication between port 8080 clinic app and port 8081 billing service |
| NFR-08 | Database sophistication | View, procedure, function and triggers for MySQL/MariaDB |
| NFR-09 | Reliability | Billing health check, clear service-unavailable messages and global error handling |
