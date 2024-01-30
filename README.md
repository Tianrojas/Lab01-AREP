# Lab01-AREP

Este proyecto consiste en una aplicación de red que incluye un servidor HTTP asíncrono, un manejador de solicitudes HTTP y un caché para almacenar respuestas previas.

## Descripción General del Diseño

La arquitectura del proyecto está diseñada para manejar solicitudes HTTP entrantes de clientes y proporcionar respuestas basadas en esas solicitudes. Aquí hay un resumen de los componentes principales:

- **Servidor HTTP Asíncrono**: Este servidor escucha en un puerto específico y acepta conexiones de clientes entrantes. Utiliza múltiples subprocesos para manejar múltiples clientes de manera simultánea sin bloquear el hilo principal.
- **Manejador de Solicitudes HTTP**: Este componente procesa las solicitudes HTTP recibidas por el servidor. Analiza la solicitud para determinar el método HTTP y la URI solicitada, luego genera una respuesta apropiada en función de la solicitud.
- **Caché**: El caché almacena las respuestas previamente obtenidas para evitar realizar solicitudes redundantes al servidor externo. Mejora el rendimiento al proporcionar respuestas almacenadas en caché cuando sea posible.

## Arquitectura y Patrones de Diseño

El proyecto sigue una arquitectura basada en el modelo cliente-servidor, donde el servidor HTTP asíncrono actúa como el servidor que escucha las solicitudes entrantes de los clientes. A continuación, se describen algunos de los patrones de diseño utilizados en el proyecto:

1. **Modelo Cliente-Servidor**: Esta arquitectura separa las responsabilidades entre el cliente y el servidor. El cliente envía solicitudes al servidor, y el servidor responde a esas solicitudes.
2. **Patrón Singleton**: El patrón Singleton se utiliza en la clase **Cache** para garantizar que solo haya una instancia de la caché en toda la aplicación. Esto ayuda a centralizar el almacenamiento de datos y a compartir la misma instancia de caché entre diferentes partes del código.
3. **Patrón Factory (Método Estático)**: En el manejador de solicitudes HTTP (**HttpRequestHandler**), se utiliza un método estático **handleRequest** que actúa como una fábrica para crear diferentes tipos de respuestas según el método HTTP recibido. Esto facilita la creación de instancias de objetos sin exponer la lógica de creación directamente.
4. **Patrón Estrategia**: Aunque no está explícitamente implementado como tal, el manejo de diferentes tipos de solicitudes HTTP (GET, POST, etc.) en el **HttpRequestHandler** sigue un enfoque similar al patrón Estrategia. Cada tipo de solicitud tiene su propia estrategia de manejo implementada dentro del mismo objeto.
5. **Patrón Observer (Manejo Asíncrono)**: El servidor HTTP asíncrono utiliza un enfoque basado en subprocesos para manejar múltiples clientes simultáneamente. Cada vez que se acepta una nueva conexión, se crea un nuevo hilo para manejar esa conexión de manera asíncrona, lo que sigue el principio del patrón Observer donde los observadores (subprocesos) están a la espera de eventos (nuevas conexiones).

## Estrategias de Programación Empleadas

El proyecto utiliza varias estrategias de programación para garantizar la eficiencia, la modularidad y la mantenibilidad del código. A continuación, se detallan algunas de estas estrategias:

1. **Programación Orientada a Objetos (POO)**:El código sigue los principios de la POO, como encapsulamiento, herencia y polimorfismo, para organizar la lógica del programa en clases y objetos.
    Se definen clases como **Cache**, **HttpRequestHandler**, y **HttpServerAsnc**, cada una con responsabilidades específicas y bien definidas.

2. **Manejo de Excepciones**: Se utiliza el manejo de excepciones para gestionar situaciones excepcionales de manera controlada.
    Por ejemplo, en el método **handleRequest** de **HttpRequestHandler**, se capturan excepciones de E/S (IOException) al manejar solicitudes GET.

3. **Programación Funcional**:   Se utilizan expresiones lambda y métodos de referencia para una programación más funcional en ciertos puntos del código, como en la creación de hilos en el servidor HTTP asíncrono (**HttpServerAsnc**).
    Esto permite escribir código más conciso y expresivo, especialmente en situaciones que involucran operaciones con colecciones de datos.

4. **Uso de Métodos Sincrónicos y Asíncronos**:   Se implementa un servidor HTTP asíncrono (**HttpServerAsnc**) que maneja múltiples clientes de forma concurrente utilizando hilos.
    Esto permite que el servidor maneje eficientemente múltiples solicitudes simultáneamente, lo que mejora la escalabilidad y el rendimiento del sistema.

5. **Patrón de Diseño MVC (Modelo-Vista-Controlador)**:   Aunque no está explícitamente implementado, se pueden identificar elementos de este patrón en la estructura general del proyecto.
    Por ejemplo, el servidor HTTP (**HttpServerAsnc**) actúa como el controlador que maneja las solicitudes entrantes y las enruta a los métodos apropiados para procesarlas.

## Funcionamiento

El proyecto, consiste en un servidor HTTP asíncrono que escucha en el puerto 35000 y gestiona las solicitudes entrantes de los clientes. Su funcionamiento se basa en el manejo de solicitudes HTTP GET y POST, respondiendo a ellas según la ruta especificada en la URL.

1. **Escucha del Puerto:** El servidor comienza escuchando en el puerto 35000 para las conexiones entrantes de los clientes.
2. **Manejo de Solicitudes:** Cuando un cliente se conecta, el servidor acepta la conexión y crea un nuevo hilo para manejar esa solicitud de forma asíncrona, lo que permite que el servidor maneje múltiples solicitudes simultáneamente.
3. **Análisis de la Solicitud:** El servidor analiza la solicitud HTTP entrante para determinar el método HTTP utilizado (GET o POST) y la ruta especificada en la URL.
4. **Enrutamiento:** Dependiendo de la ruta especificada, el servidor determina la acción a tomar. Por ejemplo, si la ruta es **/cliente**, el servidor responde con un formulario HTML para que el cliente ingrese datos. Si la ruta es **/movie**, el servidor procesa la solicitud GET para obtener información sobre una película desde una API externa.
5. **Manejo de Solicitudes GET:** Cuando se recibe una solicitud GET para la ruta **/movie,** el servidor extrae el nombre de la película de la URL, normaliza el nombre y consulta la caché para ver si la información ya está almacenada. Si la información está en caché, se devuelve al cliente desde la caché; de lo contrario, se realiza una solicitud HTTP externa a la API indicada para obtener la información, que luego se almacena en caché antes de enviarla al cliente. El resultado siempre estará en formato JSON.
6. **Manejo de Solicitudes POST:** El servidor aún no implementa completamente el manejo de solicitudes POST. En su lugar, devuelve un mensaje indicando que la función no está implementada (Se menciona que es opcional, pero debe haber la posibilidad de implementación o extensión).
7. **Generación de Respuestas:** Una vez que se ha obtenido la información solicitada (ya sea de la caché o de una solicitud externa), el servidor genera una respuesta HTTP adecuada, incluyendo los datos solicitados (como la información de la película) en formato HTML.
8. **Envío de Respuesta al Cliente:** Finalmente, el servidor envía la respuesta HTTP al cliente a través del socket, completando así el ciclo de solicitud-respuesta.

## Extensibilidad del Proyecto

El proyecto está diseñado con la extensibilidad en mente, lo que permite agregar nuevas funcionalidades y ampliar su alcance con relativa facilidad. A continuación se detallan algunas estrategias que hacen que el proyecto sea altamente extensible:

1. **Interfaz Clara entre Componentes**:   Los diferentes componentes del proyecto, como el servidor HTTP, el manejador de solicitudes y la caché, están diseñados para comunicarse entre sí a través de interfaces claras y definidas. Esto facilita la introducción de nuevos componentes o la modificación de los existentes sin afectar el funcionamiento general del sistema.

2. **Inyección de Dependencias**:   Se puede implementar un mecanismo de inyección de dependencias para desacoplar los componentes y permitir su fácil sustitución o modificación. Esto permite que los nuevos componentes sean intercambiables y se integren sin problemas en el sistema existente.

3. **Uso de Patrones de Diseño**:   El proyecto hace uso de patrones de diseño como el patrón de diseño de fábrica y el patrón de diseño de caché para gestionar la creación de objetos y mejorar la eficiencia del almacenamiento en caché de datos. Estos patrones proporcionan una estructura flexible que facilita la incorporación de nuevas funcionalidades.

4. **Documentación Clara y Concisa**:   La documentación detallada de cada componente y funcionalidad del proyecto facilita la comprensión de su estructura y facilita la tarea de extender o modificar el código existente.

5. **Tests Unitarios y de Integración**:   Se pueden agregar pruebas unitarias y de integración exhaustivas para validar el comportamiento esperado del sistema y garantizar que las nuevas extensiones o modificaciones no introduzcan errores o regressions en el código existente.

6. **API RestFul**:   La implementación de una API RESTful permite una fácil integración con otros sistemas y servicios, lo que facilita la expansión del proyecto para satisfacer nuevas necesidades o integrarse con otros sistemas externos.

## Buenas Prácticas

El proyecto sigue una serie de buenas prácticas de desarrollo de software para garantizar su calidad, mantenibilidad y facilidad de uso. A continuación, se detallan algunas de estas prácticas:

1. **Nomenclatura Significativa**:
   Se utilizan nombres descriptivos y significativos para variables, métodos y clases, lo que facilita la comprensión del código por parte de otros desarrolladores.
2. **Comentarios y Documentación**:
   Se incluyen comentarios claros y concisos en el código para explicar su funcionamiento y propósito. Además, se genera documentación detallada para cada método y funcionalidad del proyecto.
3. **Pruebas Unitarias**:
   Se implementan pruebas unitarias exhaustivas para validar el comportamiento de cada componente y funcionalidad del proyecto. Esto garantiza que el código funcione correctamente y ayuda a detectar errores de forma temprana.
4. **Control de Versiones**:
   Se utiliza un sistema de control de versiones, como Git, para gestionar el código fuente y realizar un seguimiento de los cambios realizados en el proyecto. Esto facilita la colaboración entre desarrolladores y permite revertir cambios en caso de ser necesario.
5. **Separación de Responsabilidades**:
   Se sigue el principio de responsabilidad única, donde cada componente o clase tiene una única responsabilidad. Esto facilita el mantenimiento del código y permite reutilizar los componentes en diferentes partes del proyecto.
6. **Gestión de Errores**:
   Se implementa un manejo adecuado de errores y excepciones para garantizar la robustez del sistema y proporcionar una experiencia de usuario consistente en caso de fallos.

## Cómo Usar Este Proyecto (con Maven)
Este proyecto se ha configurado para utilizar Maven como herramienta de administración de dependencias y construcción. A continuación, se detallan los pasos para ejecutar y probar el servidor web concurrente:

* Requisitos Previos
  Asegúrate de tener instalados los siguientes elementos en tu sistema:
  *  Java Development Kit (JDK): Se debe tener una versión del JDK instalada en el sistema.
  *  Maven: Se debe tener Maven instalado.
* Pasos para Ejecutar el Proyecto
  Clonar el Repositorio: Abrir una terminal y navegar hasta el directorio en el que se desee clonar el repositorio.
  * Compilar el Proyecto: Haciendo uso de mvn compile
  * Ejecutar el Servidor: mvn exec:java `-Dexec.mainClass="org.networking.HttpServerAsnc"`
  * Acceder a la Interfaz de Usuario: Entrando desde un navegador a `http://localhost:35000/`



