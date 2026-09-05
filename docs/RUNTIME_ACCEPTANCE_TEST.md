# Runtime Acceptance Test and Screenshot Checklist

Use this checklist on the Windows/XAMPP machine before final submission. Record **Pass/Fail**, actual result and screenshot filename. Do not fabricate results.

| ID | Function | Action | Expected result | Brief/rubric evidence |
|---|---|---|---|---|
| RT-01 | First setup | Start with empty DB | Local `/setup` creates first Admin; no default credentials | Secure authorized access |
| RT-02 | Login | Correct staff credentials | Dashboard opens | User authentication |
| RT-03 | Login validation | Wrong password | Login rejected with appropriate message | Validation/security |
| RT-04 | Role control | Receptionist opens Admin URL | Access denied | Access permissions |
| RT-05 | Dentist setup | Admin adds dentist | Saved and selectable | Data management |
| RT-06 | Treatment setup | Admin adds cost/fee/duration | Saved and selectable | Billing setup |
| RT-07 | Patient registration | Valid name/address/contact | Unique PAT number generated | Patient data |
| RT-08 | Patient validation | Invalid contact | Form rejected | Input validation |
| RT-09 | Appointment creation | Valid patient/dentist/treatment/date/time | Unique APP number generated | Register appointment |
| RT-10 | Past appointment | Use past date/time | Rejected | Validation |
| RT-11 | Clinic hours | Time before 08:00 | Rejected | Validation |
| RT-12 | Time interval | e.g. 10:10 | Rejected; 15-minute rule shown | Validation |
| RT-13 | Treatment finish | Long treatment ending after 18:00 | Rejected | Business rule |
| RT-14 | Double booking | Overlap same dentist using duration | Rejected | Prevent double booking |
| RT-15 | Appointment search | Search exact APP number | Correct record shown | Display appointment details |
| RT-16 | Appointment detail | Open View | Patient/address/contact/dentist/treatment/date/time/status shown | Complete information |
| RT-17 | Edit rule | Edit scheduled appointment | Update succeeds if valid | Interactive UI |
| RT-18 | Status complete | Mark Scheduled -> Completed | Status changes and audit entry created | Workflow |
| RT-19 | Terminal status | Try editing completed/cancelled | Rejected | Data integrity |
| RT-20 | Billing precondition | Try to bill scheduled/cancelled | Rejected | Validation |
| RT-21 | Distributed billing | Completed appointment -> Generate Bill | Clinic app calls port 8081 and stores returned total | Web service |
| RT-22 | Billing arithmetic | Compare treatment + consultation with total | Correct total | Calculate bill |
| RT-23 | One bill rule | Generate again | Existing bill returned/no duplicate | Data integrity |
| RT-24 | Receipt | Open receipt and Print | Printable receipt contains patient/appointment/fees/total | Print bill/receipt |
| RT-25 | Billing health | Stop/start billing service | Dashboard indicator changes; generation gives clear unavailable message when stopped | Distributed resilience |
| RT-26 | Reports | Select valid date range | Appointment/bill/revenue/workload/metrics shown | Decision reports |
| RT-27 | Report validation | From > To | Friendly validation message | Validation |
| RT-28 | CSV export | Export appointments | CSV downloads with correct rows | Reporting |
| RT-29 | Reminder | Create appointment within 24h | Appears in upcoming alerts | Complex/innovative feature |
| RT-30 | Advanced DB | Inspect phpMyAdmin | View/procedure/function/triggers exist | Advanced DB features |
| RT-31 | Audit log | Login/create/update/bill/logout | Admin audit page records actions | Security/professional practice |
| RT-32 | Help | Open Help | Step-by-step instructions visible | Help section |
| RT-33 | Exit | Exit System / Secure Logout | Session invalidated; login required again | Exit system/session |
| RT-34 | Session protection | Use Back after logout to protected URL | Redirected to login | Sessions/cookies |
| RT-35 | Automated tests | `mvn clean test` | BUILD SUCCESS | Task C |
| RT-36 | CI workflow | Push to GitHub | GitHub Actions passes | Task D workflow |
| RT-37 | Release | Push version tag | Release workflow packages JARs | Deployment/versioning |

## Recommended screenshot evidence
Capture only meaningful screenshots: login, patient save, appointment save/detail, overlap validation, completed appointment, generated receipt, reports, help, secure logout, automated test success, JaCoCo/Surefire output, Git history/branches, successful GitHub Actions and release/tag.
