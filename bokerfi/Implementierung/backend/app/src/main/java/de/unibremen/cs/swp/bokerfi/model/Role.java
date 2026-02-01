package de.unibremen.cs.swp.bokerfi.model;

/**
 * Enumeration der Benutzerrollen.
 *
 * <ul>
 *   <li>{@link #ADMIN}: Administrator mit vollen Rechten.</li>
 *   <li>{@link #MANAGER}: Manager, der Assets verwalten kann.</li>
 *   <li>{@link #EMPLOYEE}: Standard-Mitarbeiter.</li>
 * </ul>
 */
public enum Role {
    ADMIN,
    MANAGER,
    EMPLOYEE
}
