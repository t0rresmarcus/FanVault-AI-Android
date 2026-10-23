# FanVault Android (Kotlin, MVVM, Jetbrains Compose)

Native Android client for the FanVault RAG sports chatbot.

## Architecture
- UI: Jetpack Compose (ChatScreen, drawer with threads)
- ViewModel: ChatViewModel (single UiState StateFlow)
- Repository: ChatRepository (Room + Retrofit)
- Data: Room (threads, messages), Retrofit POST /chat {"question": ...}

## Run
1. Start backend reachable from the emulator/phone:
   `uvicorn main:app --host 0.0.0.0 --port 8000`
2. Open this folder in Android Studio (Koala or newer), let Gradle sync, run on an emulator.
   Emulator reaches your computer via http://10.0.2.2:8000/
3. Real phone: set BASE_URL in app/build.gradle.kts to http://<your-LAN-IP>:8000/
   and add that IP to res/xml/network_security_config.xml.
