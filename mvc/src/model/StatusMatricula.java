package model;


public enum StatusMatricula {
    CONFIRMADA("Confirmada"),
    SOLICITADA("Solicitada"),
    CANCELADA("Cancelada");

    private final String label;

    StatusMatricula(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        if (this == StatusMatricula.CONFIRMADA) {
            return "Confirmada";
        } else if (this == StatusMatricula.SOLICITADA) {
            return "Solicitada";
        }

        return "Cancelada";
    }

    public boolean isBlank() {
        return label.isBlank();
    }

    static public StatusMatricula createMatricula(String s) {
        if (s.equals("CONFIRMADA")) {
            return StatusMatricula.CONFIRMADA;
        } else if (s.equals("SOLICITADA")) {
            return StatusMatricula.SOLICITADA;
        }

        return StatusMatricula.CANCELADA;
    }
}