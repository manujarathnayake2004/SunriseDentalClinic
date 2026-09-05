# Deep Functional Audit

This audit checks the implemented source against the CIS6003 scenario and high-band criteria. Runtime evidence must still be captured on the student's own Windows/XAMPP environment.

| Function | Source review result | Main evidence | Runtime evidence to capture |
|---|---|---|---|
| Secure staff login | Implemented | Spring Security + BCrypt + roles | Valid/invalid login screenshots |
| No demo/default credentials | Implemented | One-time `/setup`; no seeded users | First-run setup screenshot |
| Patient registration/edit/search | Implemented | Patient controller/service/repository | Valid + invalid patient tests |
| Unique patient number | Improved | Highest existing `PAT-xxxxx` + unique key | Create two patients |
| Appointment registration | Implemented | Appointment form/controller/service | Saved appointment screenshot |
| Required appointment fields | Implemented | Patient, dentist, treatment, date/time relationships | Details page screenshot |
| Unique appointment number | Improved | Highest existing `APP-xxxxxx` + unique key | Multiple appointment screenshot |
| Appointment-number search | Implemented | Repository search and list page | Exact number search screenshot |
| Complete appointment display | Implemented | Appointment details page | Details screenshot |
| Past/out-of-hours validation | Implemented | Service + Bean Validation | Error screenshot |
| Overlap/double-booking prevention | Improved | Treatment-duration overlap rule + DB triggers | Attempt overlapping appointment |
| Appointment status flow | Implemented | SCHEDULED → COMPLETED/CANCELLED | Status screenshots |
| Distributed web service | Implemented | REST billing service on 8081 | Both services running + health indicator |
| Billing calculation | Implemented | REST Strategy + DB function verification | Receipt totals screenshot |
| Duplicate bill prevention | Implemented | One-to-one DB constraint + service reuse | Try billing same appointment twice |
| Print receipt | Implemented | Dedicated receipt + `window.print()` | Print preview/PDF screenshot |
| Help section | Implemented | 10-step help page | Help page screenshot |
| Exit system | Implemented | POST logout + invalidation + cookie deletion | Logout/login screen screenshot |
| Reports | Improved | Appointment, workload, completion, revenue | Report screenshot |
| CSV export | Added | Stored procedure + fallback | Downloaded CSV screenshot |
| Upcoming reminder alerts | Added | Next-24-hour dashboard panel | Dashboard alert screenshot |
| Billing service monitoring | Added | REST health check | ONLINE/OFFLINE demonstration |
| Admin role restrictions | Implemented | URL + template authorization | Receptionist access denied screenshot |
| Staff user management | Implemented | Admin-only CRUD + last-admin protection | Staff management screenshot |
| Dentist/treatment management | Improved | Admin CRUD + duplicate-name checks | Forms/list screenshots |
| Audit log | Improved | Login/failure/logout + CRUD actions | Audit page screenshot |
| Advanced DB features | Improved | auto installer + SQL script + real app usage | phpMyAdmin routines/triggers/view screenshot |
| Error handling | Improved | business messages + branded global error page | controlled validation/error screenshot |
| Automated testing | Improved | unit + API + context + security tests | Maven/CI passing screenshot |
| CI/CD | Implemented | GitHub Actions CI + release workflow | Workflow and Release screenshots |

## Important
A source review can establish that the functions are implemented, but it cannot honestly replace real execution evidence. Before submission, run the final manual test plan and capture screenshots of actual results.
