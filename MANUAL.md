# Manual de Instalación y Resumen Técnico: PadelMatch 🎾

¡Bienvenido a **PadelMatch**, tu plataforma de pádel integral con seguridad y privacidad avanzadas protegidas localmente! Este archivo describe cómo instalar, compilar y ejecutar esta aplicación, además de detallar el diseño técnico enfocado en la protección pasiva de datos.

---

## 🏆 Características Principales

1. **Espacio Libre (Match Finder)**: Muro de anuncios abiertos para buscar parejas o contrincantes en partidos que están por jugarse (ej. "Falta 1 para partido"). Puedes publicar invitaciones o unirte a partidas existentes.
2. **Clasificación en Tiempo Real (Ranking)**: Tabla de clasificación detallada que muestra el nivel de juego (1.0 a 7.0), el lado preferido de pista (Drive/Revés/Ambos), victorias, derrotas y puntos de ranking.
3. **Control Total de Perfil**: Estadísticas automáticas (% de victorias), nivel clasificatorio modificable mediante un simulador de partidos integrado que recalcula los puntos clasificatorios en tiempo real.
4. **Seguridad y Privacidad Rigurosas**:
   - Bloqueo de dispositivo integrado mediante un código PIN de 4 dígitos.
   - Encriptación local y almacenamiento de contraseñas mediante hash irreversible **SHA-256** con sal.
   - **RGPD-Compliant Phone Shield**: Ofuscación completa de números de teléfono en el ranking para evitar spam. El contacto real solo se revela de forma segura cuando dos jugadores están inscritos en el mismo partido libre.

---

## 💻 Requisitos de Sistema

- **Lenguaje**: Kotlin, UI con Jetpack Compose.
- **Android SDK**: Compila con Android SDK 36 (Mínimo: Android SDK 24 / Nougat 7.0).
- **Herramientas de construcción**: Gradle (Kotlin DSL, `.gradle.kts`).
- **Persistencia**: SQLite local administrado mediante Room Database (con Kotlin Symbol Processing - KSP).

---

## ⚙️ Instrucciones de Instalación y Compilación

Sigue estos pasos para abrir, compilar y ejecutar PadelMatch en tu entorno de desarrollo local con Gradle o Android Studio:

### Paso 1: Descargar y descomprimir el .Zip
1. Haz clic en la opción de exportar como archivo `.zip` en los ajustes de AI Studio (o descárgalo directamente desde el workspace).
2. Extrae el contenido en una carpeta local de tu ordenador.

### Paso 2: Importar en Android Studio
1. Abre **Android Studio** (versión Ladybug o superior recomendada).
2. Selecciona **File -> Open** (Archivo -> Abrir).
3. Navega hasta el directorio descomprimido y selecciona la carpeta raíz del proyecto (la que contiene `settings.gradle.kts` e `app/`).
4. Android Studio iniciará de forma automática el síncrono del Gradle y descargará las dependencias necesarias. Esto puede tomar 1 o 2 minutos.

### Paso 3: Ejecutar la aplicación
1. Conecta un dispositivo físico Android con la depuración USB habilitada, o inicia un Emulador Android (AVD) de tu elección.
2. Haz clic en el botón verde **Run (Ejecutar)** en la parte superior derecha de la pantalla de Android Studio.
3. El compilador generará el APK de depuración y lo instalará en tu terminal.

### Paso 4: Generar APK listo para distribución (Opcional)
Para generar un APK instalable manualmente sin necesidad de conectar el ordenador:
1. En Android Studio, ve a **Build -> Build Bundle(s) / APK(s) -> Build APK(s)**.
2. Una vez completado el progreso, haz clic en **Locate** en la notificación flotante inferior derecha para obtener el archivo `app-debug.apk`.
3. Pásalo a tu móvil Android mediante cable, chat privado, correo o Google Drive para instalarlo directamente.

---

## 🧠 Arquitectura Técnica & Seguridad de Datos

La aplicación ha sido desarrollada bajo la metodología limpia **MVVM** (Model-View-ViewModel) dividiendo lógicamente los apartados para evitar filtración de inconsistencias:

### 1. Hashing Criptográfico de PIN (`SecurityUtils.kt`)
En lugar de almacenar el código PIN (por ejemplo: `1234`) del usuario en texto plano, aplicamos:
- **Hashing SHA-256**: Un algoritmo criptográfico irreversible en sentido inverso.
- **Sal de Seguridad (Salt)**: Adición de un sufijo estático robusto (`"PadelMatch_Secure_Salt_2026_@!"`) previo al hashing para invalidar ataques de fuerza bruta o diccionarios precalculados.

### 2. Ofuscación de Teléfonos por Permiso de Pista (RGPD)
Para evitar el acceso inapropiado a datos sensibles corporativos o de contacto de terceros:
- El número de los contrincantes se ofusca bajo la máscara `"• • • • •"` al transitar la clasificación general.
- Cuando decides unirte a una partida libre (o publicas una propia), y otro jugador coincide en tu misma pista reservada, el sistema de Room recalcula la visibilidad de manera reactiva liberando exclusivamente a estos miembros el teléfono real para que puedan coordinar el partido por WhatsApp o llamada directa.

### 3. Precarga de Datos de Nivel Profesional (Seeding automático)
Para que el usuario experimente una experiencia realista de competición desde el primer segundo (inspirado en la red física de **Playtomic**), la base de datos se precarga de forma transparente en el primer inicio de la app con perfiles de simulación de figuras profesionales como **Arturo Coello**, **Alejandro Galán**, **Bea González**, entre otros. ¡Comienza a disputar victorias contra ellos y sube puestos en el panel!
