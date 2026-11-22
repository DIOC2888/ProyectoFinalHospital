package org.example.proyectofinalhospital.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita", schema = "administracion")
public class Citas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "motivo", length = 255)
    private String motivo;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "PROGRAMADA";

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_paciente",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cita_paciente"))
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_medico",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cita_medico"))
    private Medico medico;

    public Citas() {
    }

    public Citas(LocalDateTime fechaHora, String motivo,
                 String estado, String observaciones,
                 LocalDateTime fechaRegistro,
                 Paciente paciente, Medico medico) {

        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fechaRegistro = fechaRegistro;
        this.paciente = paciente;
        this.medico = medico;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}
