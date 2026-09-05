# Test Plan and Requirements Traceability

Actual Result, Pass/Fail and screenshot evidence must be completed from real execution. This document does not fabricate results.

## Test rationale
The system uses layered testing because the highest-risk rules are business rules (double booking, status transitions, billing eligibility and access control), while the distributed billing API also needs endpoint validation. Unit tests isolate business logic; integration/MockMvc tests check Spring wiring and authorization; manual tests verify user experience and printing. GitHub Actions automates the suite and JaCoCo produces coverage evidence.

## TDD evidence to demonstrate in report
For at least two rules, show:
1. **RED** — write a failing test first (e.g., overlapping appointment should be rejected).
2. **GREEN** — implement minimum logic until the test passes.
3. **REFACTOR** — simplify the service while keeping the test green.

Recommended TDD examples: duration-aware booking overlap and completed-only billing.

## Automated test cases
| ID | Rule | Test class | Expected result |
|---|---|---|---|
| AT-01 | Valid appointment creation | AppointmentServiceTest | Saved with unique number |
| AT-02 | Continue ID after previous records | AppointmentServiceTest | No identifier reuse |
| AT-03 | Duration-aware overlap prevention | AppointmentServiceTest | Overlap rejected |
| AT-04 | Clinic opening hours | AppointmentServiceTest | Before 08:00 rejected |
| AT-05 | Finish before closing | AppointmentServiceTest | Treatment ending after 18:00 rejected |
| AT-06 | 15-minute scheduling interval | AppointmentServiceTest | Invalid minute rejected |
| AT-07 | Past appointment | AppointmentServiceTest | Rejected |
| AT-08 | Inactive dentist | AppointmentServiceTest | Rejected |
| AT-09 | Cancelled appointment billing | BillServiceTest | Rejected |
| AT-10 | Scheduled appointment billing | BillServiceTest | Must complete first |
| AT-11 | REST billing quote creates bill | BillServiceTest | Correct bill saved |
| AT-12 | DB function vs REST total | BillServiceTest | Mismatch rejected |
| AT-13 | Existing bill reuse | BillServiceTest | No duplicate bill/API call |
| AT-14 | Billing Strategy total | BillingCalculationServiceTest | Treatment + consultation total |
| AT-15 | REST billing endpoint | BillingApiControllerTest | Valid JSON returns total |
| AT-16 | REST input validation | BillingApiControllerTest | Negative amount returns HTTP 400 |
| AT-17 | First system setup | SystemSetupServiceTest | Initial ADMIN created securely |
| AT-18 | Prevent second setup | SystemSetupServiceTest | Rejected |
| AT-19 | Password confirmation | SystemSetupServiceTest | Mismatch rejected |
| AT-20 | Protect final active admin | UserAccountServiceTest | Demotion/disable rejected |
| AT-21 | Patient number continuity | PatientServiceTest | Correct next PAT number |
| AT-22 | Report completion metrics | ReportServiceTest | Correct totals/rate |
| AT-23 | Spring application wiring | ClinicApplicationContextTest | Context loads using H2 profile |
| AT-24 | Protected page authentication | SecurityAuthorizationIntegrationTest | Anonymous user redirected |
| AT-25 | Receptionist admin restriction | SecurityAuthorizationIntegrationTest | Access denied |
| AT-26 | Admin authorization | SecurityAuthorizationIntegrationTest | Admin page opens |

## Manual functional test plan
| ID | Function | Test data/category | Expected result |
|---|---|---|---|
| FT-01 | First setup | new empty database | Local setup page creates first ADMIN |
| FT-02 | Login | valid authorized credentials | Dashboard opens |
| FT-03 | Login | wrong password | Clear error; login failure audited |
| FT-04 | Patient | valid name/address/contact | Saved with unique patient number |
| FT-05 | Patient | invalid contact | Validation shown, no save |
| FT-06 | Appointment | valid future slot | Saved with unique appointment number |
| FT-07 | Appointment | same/overlapping dentist period | Booking rejected |
| FT-08 | Appointment | past/out-of-hours/time interval | Validation shown |
| FT-09 | Search | exact appointment number | Matching complete record shown |
| FT-10 | Status | scheduled → completed | Status updated |
| FT-11 | Status | scheduled → cancelled | Status updated; slot becomes reusable |
| FT-12 | Billing | scheduled appointment | Billing rejected |
| FT-13 | Billing | completed appointment + service online | Correct bill generated |
| FT-14 | Billing | service offline | Clear service-unavailable message |
| FT-15 | Receipt | generated bill | Printable receipt opens |
| FT-16 | Reports | valid date range | Metrics/tables/revenue generated |
| FT-17 | Reports | From > To | Validation message |
| FT-18 | CSV export | valid date range | CSV downloads with report rows |
| FT-19 | Advanced DB | phpMyAdmin routines/view/triggers | Features visible and queryable |
| FT-20 | Reminder | appointment within 24 hours | Dashboard alert appears |
| FT-21 | Authorization | Receptionist opens admin URL | Access denied |
| FT-22 | Audit | login/edit/status/bill/logout | Actions listed with user/time |
| FT-23 | Logout/exit | authenticated session | Session invalidated and login shown |
| FT-24 | Responsive UI | desktop + narrow browser | Pages remain usable |

## Requirements traceability
| Requirement | Implementation | Verification |
|---|---|---|
| Secure authorized login | SecurityConfig/UserAccount/BCrypt | AT-24–26, FT-02/03/21 |
| Register patient | PatientController/Service | AT-21, FT-04/05 |
| Register appointment | AppointmentController/Service | AT-01–08, FT-06–08 |
| Unique appointment number | service generator + unique constraint | AT-01/02 |
| Prevent double booking | duration overlap + triggers | AT-03, FT-07 |
| Search/display appointment | repository search + details | FT-09 |
| Calculate bill | BillingClient + REST Strategy | AT-09–16, FT-12–14 |
| Print receipt | receipt template | FT-15 |
| Help | help template | manual inspection |
| Exit/logout | Spring Security logout | FT-23 |
| Reports | ReportService/advanced DB | AT-22, FT-16–19 |
| Sessions/cookies | Spring Security + application properties | FT-02/21/23 |
| Test automation | Maven/JUnit/GitHub Actions/JaCoCo | CI evidence |
