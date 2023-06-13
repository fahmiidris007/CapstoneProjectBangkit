# Guide to Cloning and Running CalMind App in Android Studio

This guide will walk you through the step-by-step process of cloning the repository directly in Android Studio and running it on the emulator.

## Prerequisites

Before you begin, please ensure that you have met the following requirements:
- Android Studio is installed on your computer. If you haven't installed it yet, you can download it from the official Android Studio website: [Download Android Studio](https://developer.android.com/studio).
- An Android emulator is set up and configured, or an Android device is connected to your computer.

## Steps

Follow the steps below to clone the CapstoneProjectBangkit repository and run it in Android Studio:

### 1. Clone the Repository

1. Open Android Studio on your computer.
2. On the welcome screen, click on "Get from Version Control" or navigate to "File" -> "New" -> "Project from Version Control" -> "Git".
3. In the "URL" field, paste the following repository URL: `https://github.com/fahmiidris007/CapstoneProjectBangkit.git`.
4. Choose the directory where you want to clone the repository.
5. Click on the "Clone" button to initiate the cloning process.

### 2. Open the Project

1. After the cloning process is complete, Android Studio will automatically detect the project and prompt you to open it. Click on "Yes" to open the project.
2. If Android Studio doesn't prompt you, you can manually open the project by selecting "File" -> "Open" from the top menu, and then navigating to the directory where you cloned the repository.

### 3. Set Up the Emulator

1. Make sure that you have set up an emulator in Android Studio. You can do this by clicking on the "AVD Manager" icon in the toolbar or by selecting "Tools" -> "AVD Manager" from the top menu.
2. If you don't have an emulator set up, click on the "+ Create Virtual Device" button in the AVD Manager and follow the prompts to create a new emulator with the desired specifications.
3. Ensure that the emulator is running or that your Android device is connected and recognized by Android Studio.

### 4. Build and Run the Project

1. Once the project is open in Android Studio, wait for the Gradle build process to finish. You can monitor the progress in the bottom console.
2. Once the build is successful, select the target device (emulator or connected Android device) from the toolbar's device dropdown menu.
3. Click on the "Run" button (green play icon) in the toolbar or select "Run" -> "Run 'app'" from the top menu.
4. Android Studio will build the project, install the app on the selected device, and launch it automatically.
5. Wait for a few moments, and the app will be up and running on the emulator or connected device.

Now you can explore and test the app's features.
