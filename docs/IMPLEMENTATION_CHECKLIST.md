# Program Implementation Checklist

## Scenario functions
- [x] Secure username/password login
- [x] Authorized staff only
- [x] No demo/default credentials
- [x] First-run administrator setup
- [x] Register/maintain patient
- [x] Register appointment
- [x] Robust unique appointment number
- [x] Patient name, address and contact linked to appointment
- [x] Dentist and treatment selection
- [x] Appointment date/time
- [x] Search using appointment number
- [x] Display complete appointment information
- [x] Calculate treatment + consultation fee
- [x] Printable receipt
- [x] Step-by-step Help Guide
- [x] Exit System / Secure Logout

## Task B / Excellent-band software evidence
- [x] Java 21 Spring Boot interactive application
- [x] Three-tier architecture
- [x] XAMPP MySQL/MariaDB relational database
- [x] Distributed REST billing service on separate port
- [x] MVC / Repository / Service Layer / Strategy patterns
- [x] Bean validation + business-rule messages
- [x] Past/out-of-hours/15-minute interval validation
- [x] Duration-aware overlapping appointment prevention
- [x] DB trigger protection
- [x] BCrypt hashing + role authorization
- [x] Cookie/session controls + secure logout
- [x] Appointment/workload/completion/revenue reports
- [x] CSV report export
- [x] Reporting view + stored procedure + function + triggers
- [x] Advanced DB features used by UI/billing verification
- [x] Upcoming appointment alerts
- [x] Billing service health monitoring
- [x] Audit log including authentication events
- [x] User-friendly global error handling

## Task C evidence
- [x] JUnit/Mockito service tests
- [x] REST endpoint/validation tests
- [x] Spring context integration test
- [x] Security authorization integration tests
- [x] H2 isolated test profile
- [x] GitHub Actions test automation
- [x] JaCoCo coverage output
- [x] Test plan + traceability
- [ ] Run `mvn clean test` on final Windows machine and capture screenshot
- [ ] Capture RED -> GREEN -> REFACTOR evidence
- [ ] Record actual results/pass-fail in report

## Task D evidence
- [x] `.gitignore` excludes IDE/build/secret files
- [x] CI workflow
- [x] test/coverage artifacts
- [x] tagged-release workflow with JAR assets
- [ ] Confirm public GitHub repository URL
- [ ] Push genuine feature-by-feature commits
- [ ] Demonstrate branches and pull requests
- [ ] Create version tags/releases
- [ ] Capture workflow runs
- [ ] Demonstrate latest runnable/released version in report
