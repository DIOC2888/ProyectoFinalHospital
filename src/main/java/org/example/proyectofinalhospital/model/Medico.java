package org.example.proyectofinalhospital.model;

import jakarta.persistence.*;

@Entity
@Table(name = "medico", schema = "administracion")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Integer idMedico;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "codigo_medico", nullable = false,
            unique = true, length = 30)
    private String codigoMedico;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_especialidad",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_medico_especialidad"))
    private Especialidad especialidad;

    public Medico() {

    }

    public Medico(String nombres, String apellidos, String codigoMedico, String telefono, String correo, Boolean activo, Especialidad especialidad) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.codigoMedico = codigoMedico;
        this.telefono = telefono;
        this.correo = correo;
        this.activo = activo;
        this.especialidad = especialidad;
    }

    public Integer getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCodigoMedico() {
        return codigoMedico;
    }

    public void setCodigoMedico(String codigoMedico) {
        this.codigoMedico = codigoMedico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }
}
