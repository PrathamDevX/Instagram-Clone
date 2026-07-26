# Fix Navigation Crash in OtpScreen

The app closes (crashes) when clicking "Next" in the `OtpScreen` because the target navigation route `password` (defined as `Routes.Password`) is not registered in the `NavHost` within `App.kt`.

## User Review Required

> [!IMPORTANT]
> The fix involves registering the `PasswordCreation` screen in the navigation graph. This is a critical fix to prevent the app from crashing during the signup flow.

## Proposed Changes

### Navigation

#### [MODIFY] [App.kt](file:///home/pratham/Documents/AndroidStudioProjects/InstagramClone/app/src/main/java/com/example/instagramclone/App.kt)

- Add import for `com.example.instagramclone.feature.signup.PasswordCreation`.
- Add `composable(Routes.Password)` to the `NavHost` to handle navigation to the password creation screen.

## Verification Plan

### Automated Tests
- Build the project to ensure there are no compilation errors.
- Run the app and navigate through the signup flow to the `OtpScreen`, then click "Next" to verify it navigates to the Password screen instead of crashing.

### Manual Verification
- Deploy to the emulator/device.
- Navigate: Login -> Create new account -> Enter Mobile/Email -> Verify OTP -> Click Next.
- Verify that the app transitions to the "Create a password" screen.
