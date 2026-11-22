# Contrato de las vistas (fx:id y acciones)

Cada controller debe declarar estos campos con `@FXML` y estos métodos con `@FXML` (el nombre del método es el valor de `onAction="#nombre"`).

Paquete esperado de los controllers: `org.example.proyectofinalhospital.controller`


## PacienteView.fxml → `PacienteController`

| Tipo | fx:id |
|---|---|
| text | `txtNombres` |
| text | `txtApellidos` |
| text | `txtDocumento` |
| date | `dpFechaNacimiento` |
| text | `txtTelefono` |
| text | `txtCorreo` |
| text | `txtDireccion` |
| text | `txtContactoEmergencia` |
| text | `txtTelefonoEmergencia` |
| check | `chkActivo` |
| button | `btnGuardar` |
| button | `btnActualizar` |
| button | `btnLimpiar` |
| button | `btnDesactivar` |
| text | `txtBuscar` |
| button | `btnBuscar` |
| table | `tblPacientes` |
| column | `colId` |
| column | `colNombres` |
| column | `colApellidos` |
| column | `colDocumento` |
| column | `colFechaNacimiento` |
| column | `colTelefono` |
| column | `colCorreo` |
| column | `colActivo` |

Métodos `onAction`: `guardar()`, `actualizar()`, `limpiar()`, `desactivar()`, `buscar()`

## MedicoView.fxml → `MedicoController`

| Tipo | fx:id |
|---|---|
| text | `txtNombres` |
| text | `txtApellidos` |
| text | `txtCodigoMedico` |
| combo | `cmbEspecialidad` |
| text | `txtTelefono` |
| text | `txtCorreo` |
| check | `chkActivo` |
| button | `btnGuardar` |
| button | `btnActualizar` |
| button | `btnLimpiar` |
| button | `btnDesactivar` |
| text | `txtBuscar` |
| button | `btnBuscar` |
| table | `tblMedicos` |
| column | `colId` |
| column | `colCodigo` |
| column | `colNombres` |
| column | `colApellidos` |
| column | `colEspecialidad` |
| column | `colTelefono` |
| column | `colCorreo` |
| column | `colActivo` |

Métodos `onAction`: `guardar()`, `actualizar()`, `limpiar()`, `desactivar()`, `buscar()`

## EspecialidadView.fxml → `EspecialidadController`

| Tipo | fx:id |
|---|---|
| text | `txtNombre` |
| area | `txtDescripcion` |
| button | `btnGuardar` |
| button | `btnActualizar` |
| button | `btnLimpiar` |
| button | `btnEliminar` |
| table | `tblEspecialidades` |
| column | `colId` |
| column | `colNombre` |
| column | `colDescripcion` |

Métodos `onAction`: `guardar()`, `actualizar()`, `limpiar()`, `eliminar()`

## CitasView.fxml → `CitasController`

| Tipo | fx:id |
|---|---|
| combo | `cmbPaciente` |
| combo | `cmbMedico` |
| date | `dpFecha` |
| combo | `cmbHora` |
| text | `txtMotivo` |
| combo | `cmbEstado` |
| area | `txtObservaciones` |
| button | `btnProgramar` |
| button | `btnActualizar` |
| button | `btnLimpiar` |
| button | `btnCancelarCita` |
| text | `txtBuscar` |
| button | `btnBuscar` |
| table | `tblCitas` |
| column | `colId` |
| column | `colPaciente` |
| column | `colMedico` |
| column | `colFecha` |
| column | `colHora` |
| column | `colMotivo` |
| column | `colEstado` |

Métodos `onAction`: `programar()`, `actualizar()`, `limpiar()`, `cancelarCita()`, `buscar()`

## UsuarioView.fxml → `UsuarioController`

| Tipo | fx:id |
|---|---|
| text | `txtUsername` |
| password | `pfContrasena` |
| combo | `cmbRol` |
| check | `chkActivo` |
| button | `btnGuardar` |
| button | `btnActualizar` |
| button | `btnLimpiar` |
| button | `btnDesactivar` |
| table | `tblUsuarios` |
| column | `colId` |
| column | `colUsername` |
| column | `colRol` |
| column | `colActivo` |
| column | `colFechaCreacion` |

Métodos `onAction`: `guardar()`, `actualizar()`, `limpiar()`, `desactivar()`

## LoginView.fxml → `LoginController`

| Tipo | fx:id |
|---|---|
| text | `txtUsuario` |
| password | `pfContrasena` |
| label | `lblError` |
| button | `btnIniciarSesion` |

Métodos `onAction`: `iniciarSesion()`

## DashboardView.fxml → `DashboardController`

| Tipo | fx:id |
|---|---|
| button | `btnInicio` |
| button | `btnPacientes` |
| button | `btnMedicos` |
| button | `btnEspecialidades` |
| button | `btnCitas` |
| button | `btnUsuarios` |
| button | `btnCerrarSesion` |
| label | `lblSeccion` |
| label | `lblUsuarioActual` |
| stackpane | `contentArea` |
| vbox | `panelBienvenida` |
| label | `lblBienvenida` |
| label | `lblTotalPacientes` |
| label | `lblMedicosActivos` |
| label | `lblCitasProgramadas` |

Métodos `onAction`: `mostrarInicio()`, `mostrarPacientes()`, `mostrarMedicos()`, `mostrarEspecialidades()`, `mostrarCitas()`, `mostrarUsuarios()`, `cerrarSesion()`
