# Práctica 1: Revisiones estáticas de código con SonarCloud, SonarQube y GitHub
## 1. Objetivo
Introducir el uso de herramientas de análisis estático de código y su integración en el flujo de desarrollo de software, utilizando SonarQube Cloud como plataforma principal.

## 2. Introducción a las revisiones estáticas de código
Las **revisiones estáticas de código** consisten en analizar el software **sin ejecutarlo**, con el objetivo de detectar defectos, malas prácticas o riesgos de seguridad. Este tipo de análisis forma parte del proceso de **aseguramiento de la calidad** y permite localizar problemas en fases tempranas del desarrollo.

Entre los beneficios de la revisión estática destacan:
- Identificación de **bugs potenciales** (uso incorrecto de variables, estructuras incompletas, excepciones no tratadas).
- Control de **estilo y convenciones de codificación** (nombres de variables, estructura de clases, redundancias).
- Detección de **código duplicado** o mal estructurado.
- **Seguridad**: vulnerabilidades comunes como inyecciones o uso inseguro de librerías.
- Mejora de la **mantenibilidad** y reducción de la **deuda técnica**.

La automatización de este proceso es posible gracias a herramientas como **SonarQube**, que integran motores de análisis estático con la posibilidad de establecer métricas de calidad y gates (umbrales mínimos que el código debe cumplir antes de ser aceptado).

![Ejemplo de análisis estático](imagenes/01_practica_01.png)

---

## 3. SonarQube Cloud: concepto y creación de una cuenta gratuita
**SonarQube Cloud** es la versión en la nube del servidor SonarQube. Permite:
- Analizar proyectos directamente conectados a repositorios de GitHub, GitLab, Azure DevOps o Bitbucket. En la versión gratuita con limitaciones
- Definir **Quality Profiles** (conjuntos de reglas activas) y **Quality Gates** (criterios de aceptación). En la versión gratuita se pueden definir pero el uso de los perfiles y criterios modificados solo está permitido si se adopta la versión de pago.
- Generar paneles de control con métricas de calidad, seguridad y cobertura de tests. Incluido en la versión gratuita.
- Integrar resultados de los análisis en los flujos de integración continua. Incluido en la ersión gratuita con limitaciones

### 2.1. Creación de cuenta en SonarQube Cloud
1. Si no se dispone de una, crearemos un cuenta de **GitHub**, usando la cuenta de correo del alumno que ejerce como coordinador del grupo. Accedemos a la cuenta de github. 
2. Con la cuenta de github abieta se accede a [https://sonarcloud.io/login](https://sonarcloud.io/login).  
3. Inicia sesión usando tu cuenta de **GitHub**.  
4. Autoriza a SonarCloud a acceder a tus repositorios. Durante este procedimiento importaremos una organización que coincidirá con el nombre del usuario propietario de la cuenta de GitHub utilizada.

![Proceso de creación de cuenta en SonarCloud](imagenes/01_practica_01.png)

---

## 3. Plugin de SonarQube para Eclipse
Para trabajar en local con las **mismas reglas y configuraciones** que tengamos en SonarCloud, se utiliza el plugin oficial **SonarQube for IDE** (antes conocido como SonarLint).

### 3.1. Instalación
- En Eclipse: **Help > Eclipse Marketplace…**.  
- Buscar “SonarQube” e instalar **SonarQube for IDE**.  
- Reiniciar Eclipse.

### 3.2. Conexión con SonarCloud
1. Ir a **Window > Preferences > SonarQube > Connected Mode**.  
2. Crear una nueva conexión con **SonarCloud**.  
3. Autenticarse con un token personal de SonarCloud.  
4. Enlazar (bind) el proyecto local de Eclipse con su proyecto correspondiente en SonarCloud.  

De este modo, Eclipse descarga el **Quality Profile** activo en SonarCloud y lo aplica a los análisis locales.  
Al editar un fichero y guardar, los **issues** aparecen en la vista de SonarQube del IDE.

![Conexión de SonarQube for IDE a SonarCloud](imagenes/captura_eclipse_sonarqube.png)

---

## 4. Creación de un repositorio en GitHub y conexión con SonarCloud
Para automatizar el análisis en cada push o pull request, necesitamos vincular nuestro repositorio en GitHub con el proyecto de SonarCloud.

### 4.1. Crear repositorio vacío en GitHub
1. Inicia sesión en [https://github.com](https://github.com).  
2. Clic en **New repository**.  
3. Define el nombre (ej. `p1-calidad-grupo01`).  
4. No marques la opción de inicializar con README para evitar conflictos iniciales.  
5. Copia la URL del repositorio (HTTPS).

![Creación de un nuevo repositorio en GitHub](imagenes/captura_crear_repo_github.png)

### 4.2. Conectar repositorio con SonarCloud
1. En SonarCloud, dentro de tu organización, selecciona **Analyze new project**.  
2. Escoge el repositorio creado en GitHub.  
3. SonarCloud configurará el proyecto y proporcionará un **SONAR_TOKEN** para autenticar los análisis.  
4. En el repositorio de GitHub:
   - Ve a **Settings > Secrets and variables > Actions**.
   - Crea un secreto llamado `SONAR_TOKEN` con el valor generado.
5. Añade un workflow de GitHub Actions:
   - En la carpeta `.github/workflows`, crea `sonarcloud.yml` con la configuración del análisis (Maven o Gradle).
   - Cada push o PR ejecutará automáticamente un análisis y publicará los resultados en SonarCloud.

![Configuración de un workflow en GitHub Actions](imagenes/captura_github_actions.png)
