# Manual de Instalación y Guía Técnica - PadelMatch 🎾

¡Bienvenido a **PadelMatch**! Esta guía te guiará paso a paso en la importación, configuración y ejecución del código fuente del proyecto en tu entorno local. Además, detalla los principios de seguridad de datos implementados.

---

## 🛠️ Requisitos del Sistema

Para compilar y ejecutar PadelMatch sin inconvenientes, asegúrate de contar con:

- **Sistema Operativo**: Windows 11/10, macOS Ventura/Sonoma, o Linux (Ubuntu 22.04 LTS o superior).
- **IDE**: [Android Studio](https://developer.android.com/studio) (Versión **Hedgehog 2023.1.1** o superior recomendada).
- **Java Development Kit (JDK)**: **Java 17** (incorporado en Android Studio).
- **Android SDK**: API Level 34 (Android 14) instalado a través del SDK Manager.
- **Dispositivo de Pruebas**: Dispositivo físico con modo Depuración USB activado, o un Emulador con API 26 (Android 8.0) como mínimo.

---

## 🚀 Pasos de Instalación e Importación

Sigue estos sencillos pasos para importar el proyecto desde el archivo `.zip` descargado:

### 1. Descomprimir el Proyecto
Extrae el contenido del archivo ZIP en una carpeta de tu preferencia en tu disco local (ej. `C:\Proyectos\PadelMatch\` o `~/Projects/PadelMatch/`).

### 2. Abrir en Android Studio
1. Abre Android Studio.
2. En la pantalla de bienvenida, haz clic en **Open** (Abrir).
3. Navega hasta el directorio donde descomprimiste el proyecto, selecciónalo y haz clic en **OK**.
4. Deja que Android Studio analice los archivos de configuración de Gradle. Esto puede tomar un par de minutos en la primera sincronización.

### 3. Configurar el JDK de Gradle
Asegúrate de que Android Studio está compilando el proyecto con Java 17:
1. Ve a **Settings / Preferences** (Ctrl + Alt + S / Cmd + ,).
2. Navega a **Build, Execution, Deployment** -> **Build Tools** -> **Gradle**.
3. En la sección **Gradle JDK**, selecciona o descarga **JetBrains Runtime 17** o **OpenJDK 17**.
4. Sincroniza Gradle haciendo clic en el icono del elefante (**Sync Project with Gradle Files**).

### 4. Ejecución del Proyecto
1. Conecta un dispositivo físico Android o inicia el Emulador desde el **Device Manager**.
2. Presiona el botón verde de reproducción **Run 'app'** (Mayús + F10 o el icono de Play) en la barra superior.
3. El archivo APK se compilará, se instalará en tu terminal y la app se abrirá automáticamente.

---

## 🔒 Arquitectura de Seguridad y Privacidad Integrada

PadelMatch se ha construido priorizando la confidencialidad de los datos de los usuarios conforme a las normativas de protección de datos (como el RGPD español). Las medidas clave de protección de datos implementadas son:

### 1. Codificación de PIN Robusta (SHA-256 + Salt)
- Los códigos PIN personales de 4 dígitos nunca se guardan en texto plano en la base de datos.
- Se procesan en la capa lógica `SecurityUtils` mediante un algoritmo criptográfico unidireccional **SHA-256** complementado con un valor de sal refinado que hace al sistema inmune a ataques de diccionario clásicos.

### 2. Ofuscación de Datos de Contacto (RGPD Matcher)
- En el **Ranking General**, los números de teléfono móvil se muestran parcialmente ofuscados (ej. `+34 600 ••• ••• 22`) para evitar vulneraciones de spam.
- **Canal Seguro de Contacto**: El sistema de base de datos evalúa dinámicamente si dos usuarios están inscritos en un partido de la sección "Espacio Libre". Solo cuando jueguen juntos en el mismo partido, se revelará el número de teléfono completo del rival para permitir coordinar el partido por teléfono de forma segura y legítima.

### 3. Almacenamiento Local Aislado
- PadelMatch utiliza **Room Database** estructurado sobre SQLite nativo con aislamiento por Sandbox a nivel de sistema operativo Android. Esto garantiza que aplicaciones de terceros no puedan leer el repositorio de datos de PadelMatch.

---

## 📂 Estructura Principal del Código

El proyecto sigue el patrón de diseño arquitectónico **MVVM (Model-View-ViewModel)** limpio y escalable desarrollado íntegramente en Jetpack Compose:

- `com.example.ui.theme.Theme.kt` y `Color.kt`: Contiene las definiciones cromáticas de la guía de estilo **Vibrant Palette** (Verde Volt `#D2FE4F`, Azul Pádel, etc.).
- `com.example.ui.screens.UnlockScreen.kt`: Gestiona el flujo de registro inicial de usuarios y la pantalla de desbloqueo mediante teclado por PIN.
- `com.example.ui.screens.MatchFeedScreen.kt`: Muestra la cartelera de "Espacio Libre", los filtros de nivel, el registro de pistas y la adhesión de jugadores a partidos vacantes.
- `com.example.ui.screens.RankingScreen.kt`: Tabla de clasificaciones oficiales del club con protección de contacto.
- `com.example.ui.screens.ProfileScreen.kt`: Panel de estadísticas individuales y simulador rápido de partidos para realizar pruebas instantáneas del flujo de puntuaciones clasificatorias.
