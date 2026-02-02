const state = {
    user: null, // { email, role, token }
    view: 'login', // login, dashboard, assets, persons, facilities, reservations, assetTypes
    assets: [],
    persons: [],
    facilities: [],
    reservations: [],
    assetTypes: [],
    loading: false,
    error: null,
    sort: { field: 'name', dir: 'asc' },
    personSort: { field: 'firstName', dir: 'asc' },
    search: { assets: '', persons: '', facilities: '', reservations: '' },
    filter: { assetsAssignedOnly: false },
    lang: localStorage.getItem('bokerfi_lang') || 'en' // Default to English
};

const translations = {
    en: {
        "nav.overview": "Overview",
        "nav.assets": "Assets",
        "nav.people": "People",
        "nav.locations": "Locations",
        "nav.bookings": "Bookings",
        "nav.types": "Types",
        "nav.profile": "My Profile",
        "nav.logout": "Sign Out",
        "login.title": "Login",
        "login.welcome": "Welcome Back",
        "login.subtitle": "Please sign in to continue",
        "login.btn": "Sign In",
        "login.btn": "Sign In",
        "login.email": "Email",
        "login.password": "Password",
        "dashboard.title": "Dashboard",
        "assets.title": "Assets",
        "assets.new": "+ New Asset",
        "people.title": "People",
        "people.new": "+ Add Person",
        "locations.title": "Locations",
        "locations.new": "+ Add Location",
        "bookings.title": "Reservations",
        "bookings.new": "+ New Booking",
        "types.title": "Asset Types",
        "types.new": "+ New Type",
        "table.actions": "Actions",
        "table.name": "Name",
        "table.status": "Status",
        "table.location": "Location",
        "table.warranty": "Warranty",
        "table.inventory": "Inventory #",
        "table.role": "Role",
        "table.pNumber": "P-Number",
        "table.start": "Start",
        "table.end": "End",
        "table.for": "For",
        "table.id": "ID",
        "table.type": "Type",
        "table.date": "Date",
        "table.assigned": "Assigned To",
        "btn.close": "Close",
        "btn.save": "Save",
        "btn.create": "Create",
        "btn.update": "Update",
        "btn.edit": "Edit",
        "btn.delete": "Delete",
        "btn.cancel": "Cancel",
        "landing.hero.title": "Orchestrating<br>Resources.",
        "landing.hero.subtitle": "Experience the next generation of asset management. Secure, efficient, and designed for the future of work.",
        "landing.enter": "Click to Enter System",
        "landing.footer.rights": "&copy; 2026 <strong>ALFA Enterprise</strong> &bull; Bibliothekstraße 1, 28359 Bremen",
        "landing.footer.privacy": "Privacy Policy",
        "landing.footer.imprint": "Imprint",
        "landing.footer.support": "Support",
        "placeholder.search.assets": "Search assets...",
        "placeholder.search.people": "Search people...",
        "placeholder.search.locations": "Search locations...",
        "placeholder.search.bookings": "Search bookings...",
        "sort.name": "Sort by Name",
        "sort.date": "Sort by Date",
        "sort.inv": "Sort by Inv #",
        "sort.type": "Sort by Type",
        "sort.fname": "Sort by First Name",
        "sort.lname": "Sort by Last Name",
        "sort.role": "Sort by Role",
        "sort.email": "Sort by Email",
        "modal.create.asset": "New Asset",
        "modal.edit.asset": "Edit Asset",
        "modal.create.person": "New Person",
        "modal.edit.person": "Edit Person",
        "modal.create.location": "New Location",
        "modal.edit.location": "Edit Location",
        "modal.create.booking": "New Booking",
        "modal.create.type": "New Type",
        "input.name": "Name",
        "input.inv": "Inventory #",
        "input.type": "Type",
        "input.location": "Location",
        "input.date": "Purchase Date",
        "input.warranty": "Warranty End",
        "input.assign": "Initial Assignment (Optional)",
        "input.assign.none": "-- Do not assign yet --",
        "empty.assets": "No assets found.",
        "empty.locations": "No locations found.",
        "empty.reservations": "No reservations found.",
        "support.title": "Support Contact",
        "support.or": "or",
        "support.email": "email",
        "status.IN_STOCK": "In Stock",
        "status.ASSIGNED": "Assigned",
        "status.MARKED_LOST": "Reported Lost",
        "status.CONFIRMED_LOST": "Lost",
        "status.MAINTENANCE_REPAIR": "In Repair",
        "status.MAINTENANCE_FINAL": "In Maintenance",
        "status.READY_FOR_INITIAL_PICKUP": "Ready for Pickup",
        "status.READY_FOR_REPAIR_PICKUP": "Ready for Pickup (Repair)",
        "status.WAITING_FOR_REPAIR_RETURN": "Waiting for Return",
        "status.WAITING_FOR_FINAL_RETURN": "Waiting for Final Return",
        "status.RETURNED_REPAIR": "Returned (Repair)",
        "status.RETURNED_FINAL": "Returned",
        "status.active": "Active",
        "status.inactive": "Inactive",
        "btn.activate": "Activate",
        "btn.deactivate": "Deactivate",
        "action.assign": "Assign",
        "action.handover": "Confirm Handover",
        "action.lost.confirm": "Confirm Lost",
        "action.return": "Return",
        "action.repair.return": "Confirm Repair Return",
        "action.maintenance.start": "Start Maintenance",
        "action.maintenance.finish": "Finish Maintenance",
        "action.defect": "Report Defect",
        "action.lost.report": "Report Lost",
        "action.lost.report": "Report Lost",
        "action.extend": "Extend Deadline",
        "action.retire": "Retire / Scrap",
        "action.return.confirm": "Confirm Return",
        "action.restock": "Restock",
        "modal.extend.deadline": "Extend Return Deadline",
        "btn.cancel.res": "Cancel",
        "toast.login.fail": "Login Failed: ",
        "login.fail.deactivated": "Account is deactivated. Please contact support.",
        "toast.asset.created": "Asset created!",
        "toast.asset.assigned": "Asset created & assigned!",
        "toast.asset.assign.fail": "Asset created but assignment failed",
        "toast.asset.updated": "Asset updated!",
        "toast.asset.retired": "Asset retired successfully",
        "toast.asset.return.marked": "Marked for Return",
        "action.return.request": "Request Return",
        "toast.asset.return.confirmed": "Return Confirmed",
        "toast.deleted": "Deleted",
        "toast.delete.fail": "Failed to delete",
        "toast.person.created": "Person Created",
        "toast.person.updated": "Person Updated",
        "toast.loc.created": "Location created",
        "toast.loc.updated": "Location updated",
        "toast.type.created": "Type created",
        "toast.res.confirmed": "Reservation confirmed",
        "toast.cancelled": "Cancelled",
        "toast.cancel.fail": "Failed to cancel",
        "toast.handover": "Handover Confirmed",
        "toast.lost.reported": "Reported as Lost",
        "toast.lost.confirmed": "Loss Confirmed",
        "toast.maint.start": "Maintenance Started",
        "table.reason": "Reason (Optional)",
        "input.return.deadline": "Return Deadline (Optional)",
    },
    de: {
        "nav.overview": "Übersicht",
        "nav.assets": "Inventar",
        "nav.people": "Personen",
        "nav.locations": "Standorte",
        "nav.bookings": "Buchungen",
        "nav.types": "Typen",
        "nav.profile": "Mein Profil",
        "nav.logout": "Abmelden",
        "login.title": "Anmelden",
        "login.welcome": "Willkommen zurück",
        "login.subtitle": "Bitte melden Sie sich an",
        "login.btn": "Anmelden",
        "login.btn": "Anmelden",
        "login.email": "E-Mail",
        "login.password": "Passwort",
        "dashboard.title": "Dashboard",
        "assets.title": "Inventar",
        "assets.new": "+ Neues Asset",
        "people.title": "Personen",
        "people.new": "+ Person hinzufügen",
        "locations.title": "Standorte",
        "locations.new": "+ Standort hinzufügen",
        "bookings.title": "Reservierungen",
        "bookings.new": "+ Neue Buchung",
        "types.title": "Asset-Typen",
        "types.new": "+ Neuer Typ",
        "table.actions": "Aktionen",
        "table.name": "Name",
        "table.status": "Status",
        "table.location": "Standort",
        "table.warranty": "Garantie",
        "table.inventory": "Inventarnummer",
        "table.role": "Rolle",
        "table.pNumber": "Personal-Nr.",
        "table.start": "Start",
        "table.end": "End",
        "table.for": "Für",
        "table.id": "ID",
        "table.type": "Typ",
        "table.date": "Datum",
        "table.assigned": "Zugewiesen an",
        "btn.close": "Schließen",
        "btn.save": "Speichern",
        "btn.create": "Erstellen",
        "btn.update": "Aktualisieren",
        "btn.edit": "Bearbeiten",
        "btn.delete": "Löschen",
        "btn.cancel": "Abbrechen",
        "landing.hero.title": "Ressourcen<br>Orchestrieren.",
        "landing.hero.subtitle": "Erleben Sie die nächste Generation des Asset Managements. Sicher, effizient und entwickelt für die Zukunft der Arbeit.",
        "landing.enter": "Klicken zum Betreten",
        "landing.footer.rights": "&copy; 2026 <strong>ALFA Enterprise</strong> &bull; Bibliothekstraße 1, 28359 Bremen",
        "landing.footer.privacy": "Datenschutzerklärung",
        "landing.footer.imprint": "Impressum",
        "landing.footer.support": "Support",
        "placeholder.search.assets": "Inventar durchsuchen...",
        "placeholder.search.people": "Personen durchsuchen...",
        "placeholder.search.locations": "Standorte durchsuchen...",
        "placeholder.search.bookings": "Buchungen durchsuchen...",
        "sort.name": "Nach Name sortieren",
        "sort.date": "Nach Datum sortieren",
        "sort.inv": "Nach Inv-Nr. sortieren",
        "sort.type": "Nach Typ sortieren",
        "sort.fname": "Nach Vorname sortieren",
        "sort.lname": "Nach Nachname sortieren",
        "sort.role": "Nach Rolle sortieren",
        "sort.email": "Nach E-Mail sortieren",
        "modal.create.asset": "Neues Asset",
        "modal.edit.asset": "Asset Bearbeiten",
        "modal.create.person": "Neue Person",
        "modal.edit.person": "Person Bearbeiten",
        "modal.create.location": "Neuer Standort",
        "modal.edit.location": "Standort Bearbeiten",
        "modal.create.booking": "Neue Buchung",
        "modal.create.type": "Neuer Typ",
        "input.name": "Name",
        "input.inv": "Inventarnummer",
        "input.type": "Typ",
        "input.location": "Standort",
        "input.date": "Kaufdatum",
        "input.warranty": "Garantieende",
        "input.assign": "Initiale Zuweisung (Optional)",
        "input.assign.none": "-- Noch nicht zuweisen --",
        "empty.assets": "Keine Assets gefunden.",
        "empty.locations": "Keine Standorte gefunden.",
        "empty.reservations": "Keine Reservierungen gefunden.",
        "support.title": "Support Kontakt",
        "support.or": "oder",
        "support.email": "E-Mail",
        "status.IN_STOCK": "Auf Lager",
        "status.ASSIGNED": "Zugewiesen",
        "status.MARKED_LOST": "Als verloren gemeldet",
        "status.CONFIRMED_LOST": "Verloren",
        "status.MAINTENANCE_REPAIR": "In Reparatur",
        "status.MAINTENANCE_FINAL": "In Wartung",
        "status.READY_FOR_INITIAL_PICKUP": "Bereit zur Abholung",
        "status.READY_FOR_REPAIR_PICKUP": "Bereit zur Abholung (Reparatur)",
        "status.WAITING_FOR_REPAIR_RETURN": "Wartet auf Rückgabe",
        "status.WAITING_FOR_FINAL_RETURN": "Wartet auf Rückgabe (Final)",
        "status.RETURNED_REPAIR": "Zurückgegeben (Reparatur)",
        "status.RETURNED_FINAL": "Zurückgegeben",
        "status.active": "Aktiv",
        "status.inactive": "Inaktiv",
        "btn.activate": "Aktivieren",
        "btn.deactivate": "Deaktivieren",
        "action.assign": "Zuweisen",
        "action.handover": "Übergabe bestätigen",
        "action.lost.confirm": "Verlust bestätigen",
        "action.return": "Rückgabe",
        "action.repair.return": "Reparatur-Rückgabe bestätigen",
        "action.maintenance.start": "Wartung starten",
        "action.maintenance.finish": "Wartung beenden",
        "action.defect": "Defekt melden",
        "action.lost.report": "Verlust melden",
        "action.lost.report": "Verlust melden",
        "action.extend": "Frist verlängern",
        "action.retire": "Aussondern / Abschreiben",
        "action.return.confirm": "Rückgabe bestätigen",
        "action.restock": "Einlagern",
        "modal.extend.deadline": "Rückgabefrist verlängern",
        "btn.cancel.res": "Stornieren",
        "toast.login.fail": "Anmeldung fehlgeschlagen: ",
        "login.fail.deactivated": "Benutzerkonto ist deaktiviert. Bitte kontaktieren Sie den Support.",
        "toast.asset.created": "Asset erstellt!",
        "toast.asset.assigned": "Asset erstellt & zugewiesen!",
        "toast.asset.assign.fail": "Asset erstellt, Zuweisung fehlgeschlagen",
        "toast.asset.updated": "Asset aktualisiert!",
        "toast.asset.retired": "Asset ausgesondert",
        "toast.asset.return.marked": "Für Rückgabe markiert",
        "action.return.request": "Rückgabe anfordern",
        "toast.asset.return.confirmed": "Rückgabe bestätigt",
        "toast.deleted": "Gelöscht",
        "toast.delete.fail": "Löschen fehlgeschlagen",
        "toast.person.created": "Person erstellt",
        "toast.person.updated": "Person aktualisiert",
        "toast.loc.created": "Standort erstellt",
        "toast.loc.updated": "Standort aktualisiert",
        "toast.type.created": "Typ erstellt",
        "toast.res.confirmed": "Reservierung bestätigt",
        "table.reason": "Grund (Optional)",
        "toast.cancelled": "Storniert",
        "toast.cancel.fail": "Stornieren fehlgeschlagen",
        "toast.handover": "Übergabe bestätigt",
        "toast.lost.reported": "Als verloren gemeldet",
        "toast.lost.confirmed": "Verlust bestätigt",
        "toast.maint.start": "Wartung gestartet",
        "input.return.deadline": "Rückgabefrist (Optional)",
    }
};

const t = (key) => translations[state.lang][key] || key;
window.toggleLanguage = () => {
    state.lang = state.lang === 'en' ? 'de' : 'en';
    localStorage.setItem('bokerfi_lang', state.lang);
    render();
};

// API Base URL (absolute for Nginx/CORS)
// API Base URL (Dynamic for remote access, assuming Backend is on 8080)
const API_BASE = `http://${window.location.hostname}:8080/api`;

// --- Utils ---
const showToast = (msg, type = 'info') => {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        document.body.appendChild(container);
    }
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `<span>${msg}</span>`;
    container.appendChild(toast);
    setTimeout(() => {
        toast.style.animation = 'fadeOut 0.3s forwards';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
};

const Modal = {
    open: (contentHtml) => {
        const overlay = document.createElement('div');
        overlay.className = 'modal-overlay';
        overlay.style.zIndex = '10000';
        overlay.onclick = (e) => { if (e.target === overlay) Modal.close(); };
        overlay.innerHTML = `<div class="modal-content card">${contentHtml}</div>`;
        document.body.appendChild(overlay);
    },
    close: () => {
        const overlay = document.querySelector('.modal-overlay');
        if (overlay) overlay.remove();
    }
};

window.showConfirm = (message) => {
    return new Promise((resolve) => {
        Modal.open(`
            <div style="text-align:center">
                <h3 style="margin-bottom:1rem">Confirmation</h3>
                <p style="margin-bottom:2rem; color:var(--text-muted)">${message}</p>
                <div style="display:grid; grid-template-columns: 1fr 1fr; gap:1rem">
                    <button class="btn-secondary" id="confirmCancel">Cancel</button>
                    <button class="btn-primary" id="confirmOk">Confirm</button>
                </div>
            </div>
        `);

        document.getElementById('confirmCancel').onclick = () => {
            Modal.close();
            resolve(false);
        };

        document.getElementById('confirmOk').onclick = () => {
            Modal.close();
            resolve(true);
        };
    });
};

const render = () => {
    const app = document.getElementById('app');
    app.innerHTML = '';

    if (state.loading) {
        app.innerHTML = '<div class="auth-container"><h2 style="animation: pulse 1s infinite">Loading...</h2></div>';
        return;
    }

    if (!state.user) {
        app.innerHTML = Views.Login();
        return;
    }

    // Layout
    const layout = document.createElement('div');
    layout.innerHTML = Components.Navbar();

    const container = document.createElement('div');
    container.className = 'content-container';

    // Router
    switch (state.view) {
        case 'dashboard': container.innerHTML = Views.Dashboard(); break;
        case 'assets': container.innerHTML = Views.Assets(); break;
        case 'persons': container.innerHTML = Views.Persons(); break;
        case 'facilities': container.innerHTML = Views.Facilities(); break;
        case 'reservations': container.innerHTML = Views.Reservations(); break;
        case 'assetTypes': container.innerHTML = Views.AssetTypes(); break;
        default: container.innerHTML = Views.Dashboard();
    }

    layout.appendChild(container);
    app.appendChild(layout);
};

const Components = {
    Navbar: () => {
        const initials = state.user.email.substring(0, 2).toUpperCase();
        return `
        <nav>
            <div class="brand-title">ALFA Enterprise</div>
            
            <div style="display:flex; align-items:center; gap:1rem">
                <div class="nav-links">
                    <a href="#" onclick="navigate('dashboard')" class="nav-item ${state.view === 'dashboard' ? 'active' : ''}">${t('nav.overview')}</a>
                    <a href="#" onclick="navigate('assets')" class="nav-item ${state.view === 'assets' ? 'active' : ''}">${t('nav.assets')}</a>
                    ${(state.user.role === 'ADMIN' || state.user.role === 'MANAGER') ? `<a href="#" onclick="navigate('persons')" class="nav-item ${state.view === 'persons' ? 'active' : ''}">${t('nav.people')}</a>` : ''}
                    <a href="#" onclick="navigate('facilities')" class="nav-item ${state.view === 'facilities' ? 'active' : ''}">${t('nav.locations')}</a>
                    <a href="#" onclick="navigate('reservations')" class="nav-item ${state.view === 'reservations' ? 'active' : ''}">${t('nav.bookings')}</a>
                    ${(state.user.role === 'ADMIN' || state.user.role === 'MANAGER') ? `<a href="#" onclick="navigate('assetTypes')" class="nav-item ${state.view === 'assetTypes' ? 'active' : ''}">${t('nav.types')}</a>` : ''}
                    
                    <div class="profile-container">
                        <div class="profile-icon" onclick="toggleProfileMenu(event)">${initials}</div>
                        <div class="profile-dropdown" id="profileDropdown">
                            <div class="profile-header">
                                <div class="profile-name">${state.user.email}</div>
                                <div class="profile-role">${state.user.role}</div>
                            </div>
                            <div class="dropdown-item" onclick="showProfile()">${t('nav.profile')}</div>
                            <div class="dropdown-item logout" onclick="logout()">${t('nav.logout')}</div>
                        </div>
                    </div>
                </div>

                <button class="glass-select btn-sm" onclick="toggleLanguage()" style="padding: 5px 10px; cursor:pointer; height:40px; margin-left: 1rem;">
                    ${state.lang === 'en' ? '🇺🇸' : '🇩🇪'}
                </button>
            </div>
        </nav>
    `;
    }
};

window.toggleProfileMenu = (e) => {
    e.stopPropagation();
    const dropdown = document.getElementById('profileDropdown');
    dropdown.classList.toggle('show');
};

// Reveal login on keypress
window.addEventListener('keydown', () => {
    const container = document.querySelector('.auth-container');
    if (container && !container.classList.contains('active')) {
        container.classList.add('active');
        container.querySelector('.auth-card').classList.add('visible');
        const hint = container.querySelector('.click-hint');
        if (hint) hint.style.opacity = '0';
    }
});

// Close dropdown when clicking outside
window.addEventListener('click', () => {
    const dropdown = document.getElementById('profileDropdown');
    if (dropdown && dropdown.classList.contains('show')) {
        dropdown.classList.remove('show');
    }
});

window.showProfile = () => {
    const person = state.persons.find(p => p.email === state.user.email) || {};
    Modal.open(`
        <div style="text-align:center">
            <div style="width:80px; height:80px; border-radius:50%; background:var(--primary); color:white; font-size:2rem; font-weight:700; display:flex; align-items:center; justify-content:center; margin:0 auto 1rem auto">
                ${state.user.email.substring(0, 2).toUpperCase()}
            </div>
            <h2>${person.firstName ? `${person.firstName} ${person.lastName}` : 'User'}</h2>
            <p style="color:var(--text-muted); margin-bottom:1.5rem">${state.user.email}</p>
            
            <div style="background:rgba(255,255,255,0.05); padding:1rem; border-radius:var(--radius-sm); text-align:left; margin-bottom:1rem">
                <div style="display:flex; justify-content:space-between; margin-bottom:0.5rem">
                    <span style="color:var(--text-muted)">Role</span>
                    <span style="font-weight:600">${state.user.role}</span>
                </div>
                 <div style="display:flex; justify-content:space-between">
                    <span style="color:var(--text-muted)">Status</span>
                    <span style="color:var(--success)">Active</span>
                </div>
            </div>
            
            <button class="btn-secondary" onclick="Modal.close()" style="width:100%">Close</button>
        </div>
    `);
};

const Views = {
    Login: () => `
        <div class="auth-container">
            <!-- Click Area for Login (Background) -->
            <div style="position:absolute; inset:0; z-index:1; cursor:pointer;" 
                 onclick="this.parentElement.classList.add('active'); this.parentElement.querySelector('.auth-card').classList.add('visible');">
            </div>

            <!-- Company Header -->
            <div class="landing-header" style="pointer-events:none; position:absolute; top:0; left:0; width:100vw; padding:2rem; z-index:10; display:flex; justify-content:flex-start; align-items:center; box-sizing:border-box;">
                <div style="font-weight:700; font-size:1.5rem; letter-spacing:-0.03em; color:white; pointer-events:auto;">ALFA <span style="font-weight:300; opacity:0.7">Enterprise</span></div>
                <button class="glass-select btn-sm" onclick="toggleLanguage()" style="pointer-events:auto; margin-left:auto; padding: 5px 10px; cursor:pointer">
                     ${state.lang === 'en' ? '🇺🇸' : '🇩🇪'}
                </button>
            </div>

            <!-- Hero Section -->
            <div class="landing-hero" style="pointer-events:none; position:absolute; top:35%; left:10%; z-index:5;">
                <h3>AlFa System Projekt</h3>
                <h1>${t('landing.hero.title')}</h1>
                <p>${t('landing.hero.subtitle')}</p>
            </div>

            <div class="click-hint" style="position:absolute; bottom:120px; color:rgba(255,255,255,0.7); text-align:center; width:100%; font-size:1.1rem; pointer-events:none; transition:opacity 0.5s; animation: pulse 2s infinite; z-index:2;">
                ${t('landing.enter')}
            </div>

            <!-- Footer Info -->
            <div class="landing-footer" style="position:absolute; bottom:2rem; width:100%; z-index:20; pointer-events:auto;">
                <div class="footer-info">
                   ${t('landing.footer.rights')}
                </div>
                <div class="footer-links" style="position:relative;">
                   <span>${t('landing.footer.privacy')}</span> &bull; <span onclick="event.stopPropagation()">${t('landing.footer.imprint')}</span> &bull; <span onclick="showSupportModal()" style="cursor:pointer; text-decoration:underline; font-weight:bold; color:var(--primary)">${t('landing.footer.support')}</span>
                </div>
            </div>

            <div class="auth-card hover-lift" style="position:relative; z-index:30;">
                <h1 style="text-align:center; margin-bottom:0.5rem">${t('login.welcome')}</h1>
                <p style="text-align:center; color:var(--text-muted); margin-bottom:2rem">${t('login.subtitle')}</p>
                <form onsubmit="handleLogin(event)">
                    <label>${t('login.email')}</label>
                    <input type="email" id="email" placeholder="example@domain.com" required>
                    <label>${t('login.password')}</label>
                    <input type="password" id="password" placeholder="••••••••" required>
                    <button type="submit" style="width:100%; margin-top:1rem">${t('login.btn')}</button>
                    ${state.error ? `<p style="color:var(--danger); margin-top:1rem; text-align:center">${state.error}</p>` : ''}
                </form>
                <div class="alfa-powered">
                    Powered by <span class="alfa-brand">AlFa</span>
                </div>
            </div>
        </div>
`,
    Dashboard: () => `
    <h2>${t('dashboard.title')}</h2>
        <div class="grid">
            <div class="card stat-card">
                <div class="stat-value">${state.assets.length}</div>
                <div class="stat-label">Total Assets</div>
            </div>
            <div class="card stat-card">
                <div class="stat-value">${state.assets.filter(a => a.status === 'ASSIGNED').length}</div>
                <div class="stat-label">Assigned</div>
            </div>
            ${(state.user.role === 'ADMIN' || state.user.role === 'MANAGER') ? `
            <div class="card stat-card">
                <div class="stat-value">${state.persons.length}</div>
                <div class="stat-label">Active Users</div>
            </div>` : ''}
            <div class="card stat-card">
                <div class="stat-value">${state.reservations.length}</div>
                <div class="stat-label">Reservations</div>
            </div>
        </div>
`,
    Assets: () => `
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem">
            <h2>${t('assets.title')}</h2>
            <div style="display:flex; gap:0.5rem; align-items:center">
                <label style="display:flex; align-items:center; gap:0.5rem; padding:0 0.8rem; height:40px; cursor:pointer; margin-bottom:0;" class="glass-panel">
                    <input type="checkbox" ${state.filter.assetsAssignedOnly ? 'checked' : ''} onchange="toggleAssignedFilter(this.checked)" style="width:auto; margin:0;">
                    <span style="font-size:0.9rem">Assigned</span>
                </label>
                
                 <div class="search-box">
                    <input type="text" id="assetSearch" placeholder="${t('placeholder.search.assets')}" value="${state.search.assets}" oninput="handleSearchInput('assets', this)">
                </div>

                <select class="glass-select" onchange="setAssetSort(this.value)">
                    <option value="name" ${state.sort.field === 'name' ? 'selected' : ''}>${t('sort.name')}</option>
                    <option value="purchaseDate" ${state.sort.field === 'purchaseDate' ? 'selected' : ''}>${t('sort.date')}</option>
                    <option value="inventoryNumber" ${state.sort.field === 'inventoryNumber' ? 'selected' : ''}>${t('sort.inv')}</option>
                    <option value="type" ${state.sort.field === 'type' ? 'selected' : ''}>${t('sort.type')}</option>
                </select>

                 ${(state.user.role === 'ADMIN' || state.user.role === 'MANAGER')
            ? `<button class="btn-primary" onclick="openCreateAssetModal()">${t('assets.new')}</button>`
            : ''}
            </div>
        </div>
    <div class="card table-container">
        <table>
            <thead>
                <tr>
                    <th onclick="setSort('inventoryNumber')" style="cursor:pointer">${t('table.inventory')} ${state.sort.field === 'inventoryNumber' ? '*' : ''}</th>
                    <th onclick="setSort('name')" style="cursor:pointer">${t('table.name')} ${state.sort.field === 'name' ? '*' : ''}</th>
                    <th>${t('table.type')}</th>
                    <th>${t('table.location')}</th>
                    <th>${t('table.warranty')}</th>
                    <th onclick="setSort('purchaseDate')" style="cursor:pointer">${t('table.date')} ${state.sort.field === 'purchaseDate' ? '*' : ''}</th>
                    <th>${t('table.status')}</th>
                    <th>${t('table.assigned')}</th>
                    <th>${t('table.actions')}</th>
                </tr>
            </thead>
            <tbody>
                ${getSortedAssets().map(a => {
                // Date Formatting Logic
                let dateDisplay = '<span style="color:var(--text-muted)">-</span>';
                if (a.purchaseDate) {
                    dateDisplay = `<div style="font-weight:600; font-size:0.95rem; color: #fff;">${a.purchaseDate}</div>`;
                    if (a.addedAt) {
                        const addedDate = a.addedAt.split('T')[0];
                        const addedTime = new Date(a.addedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
                        const subText = (addedDate === a.purchaseDate) ? `at ${addedTime}` : `${addedDate} ${addedTime}`;
                        dateDisplay += `<div style="font-size:0.75rem; color:var(--text-muted); margin-top:2px;">Added ${subText}</div>`;
                    }
                } else if (a.addedAt) {
                    dateDisplay = `<div style="font-size:0.9rem">${new Date(a.addedAt).toLocaleDateString()}</div>`;
                }
                return `
                        <tr>
                            <td style="font-family:monospace; color:var(--primary)">${a.inventoryNumber || '-'}</td>
                            <td>**${a.name}**</td>
                            <td>${a.type?.name || 'Unknown'}</td>
                            <td>${a.location?.name || 'Unknown'}</td>
                            <td>${a.warrantyEnd || '-'}</td>
                            <td>${dateDisplay}</td>
                            <td><span class="badge ${a.status.toLowerCase()}">${a.status}</span></td>
                             <td>
                                 ${a.assignedPerson ?
                        `<div>${a.assignedPerson.email}</div>
                                      ${a.assignedAt ? `<div style="font-size:0.75rem; color:var(--text-muted); margin-top:0.2rem" title="${t('table.assigned_at') || 'Assigned At'}">📥 ${a.assignedAt.replace('T', ' ').substring(0, 16)}</div>` : ''}
                                      ${a.returnDeadline ? `<div style="font-size:0.75rem; color:var(--accent); margin-top:0.1rem" title="${t('input.return.deadline') || 'Deadline'}">📅 ${a.returnDeadline.replace('T', ' ').substring(0, 16)}</div>` : ''}`
                        : '<span style="color:var(--text-muted)">-</span>'}
                             </td>
                            <td>
                                <!-- Action Menu -->
                                <div class="action-menu-container">
                                    <button class="glass-select btn-sm" style="width:auto; padding:0 0.5rem; display:flex; align-items:center; gap:0.5rem" onclick="toggleActionMenu('${a.uuid}', event)">
                                        ${t('table.actions')} ▾
                                    </button>
                                    <div id="action-menu-${a.uuid}" class="action-dropdown">
                                        ${(state.user.role === 'ADMIN' || state.user.role === 'MANAGER') ? `
                                            <div class="dropdown-item" onclick="openEditAssetModal('${a.uuid}')">${t('btn.edit')}</div>
                                            <div class="dropdown-item danger" onclick="deleteAsset('${a.uuid}')">${t('btn.delete')}</div>
                                            <div class="dropdown-divider"></div>
                                        ` : ''}

                                        <!-- Workflow Actions -->
                                        <!-- Workflow Actions -->
                                        ${(state.user.role === 'ADMIN' || state.user.role === 'MANAGER') ? `
                                            ${(a.status === 'IN_STOCK') ? `
                                                <div class="dropdown-item" onclick="openAssignModal('${a.uuid}')">${t('action.assign')}</div>
                                                <div class="dropdown-item danger" onclick="retireAsset('${a.uuid}')">${t('action.retire')}</div>
                                            ` : ''}
                                            ${(a.status === 'READY_FOR_INITIAL_PICKUP' || a.status === 'READY_FOR_REPAIR_PICKUP') ? `<div class="dropdown-item" onclick="confirmHandover('${a.uuid}')">${t('action.handover')}</div>` : ''}
                                            ${(a.status === 'MARKED_LOST') ? `<div class="dropdown-item danger" onclick="confirmLost('${a.uuid}')">${t('action.lost.confirm')}</div>` : ''}
                                            <!-- Manager can force return or confirm -->
                                            ${(a.status === 'WAITING_FOR_FINAL_RETURN') ? `<div class="dropdown-item" onclick="confirmFinalReturn('${a.uuid}')">${t('action.return.confirm')}</div>` : ''}
                                            ${(a.status === 'ASSIGNED') ? `<div class="dropdown-item" onclick="markForFinalReturn('${a.uuid}')">${t('action.return.request') || 'Request Return'}</div>` : ''}
                                            
                                            ${(a.status === 'WAITING_FOR_REPAIR_RETURN') ? `<div class="dropdown-item" onclick="confirmRepairReturn('${a.uuid}')">${t('action.repair.return')}</div>` : ''}
                                            ${(a.status === 'RETURNED_REPAIR' || a.status === 'RETURNED_FINAL') ? `
                                                <div class="dropdown-item" onclick="startMaintenance('${a.uuid}')">${t('action.maintenance.start')}</div>
                                            ` : ''}
                                            
                                            <!-- NEW: Restock/Retire from Returned Final -->

                                            
                                            ${(a.status === 'MAINTENANCE_REPAIR') ? `
                                                <div class="dropdown-item" onclick="finishMaintenance('${a.uuid}')">${t('action.maintenance.finish')}</div>
                                                <div class="dropdown-item danger" onclick="retireAsset('${a.uuid}')">${t('action.retire')}</div>
                                            ` : ''}
                                            
                                            ${(a.status === 'MAINTENANCE_FINAL') ? `
                                                <div class="dropdown-item" onclick="restock('${a.uuid}')">${t('action.restock')}</div>
                                                <div class="dropdown-item danger" onclick="retireAsset('${a.uuid}')">${t('action.retire')}</div>
                                            ` : ''}
                                        ` : `
                                            <!-- Employee Actions -->
                                            ${(a.assignedPerson && a.assignedPerson.email === state.user.email) ? `
                                                ${(a.status === 'ASSIGNED') ? `
                                                    <div class="dropdown-item" onclick="reportDefect('${a.uuid}')">${t('action.defect')}</div>
                                                    <div class="dropdown-item danger" onclick="markLost('${a.uuid}')">${t('action.lost.report')}</div>
                                                ` : ''}
                                            ` : ''}
                                        `}

                                        <!-- User Actions -->
                                        ${(state.user.role === 'ADMIN' || state.user.role === 'MANAGER') && (a.status === 'ASSIGNED' || a.status === 'READY_FOR_INITIAL_PICKUP' || a.status === 'READY_FOR_REPAIR_PICKUP') ? `
                                            <div class="dropdown-item" onclick="openExtendDeadlineModal('${a.uuid}')">${t('action.extend')}</div>
                                        ` : ''}


                                    </div>
                                </div>
                            </td>

                        </tr>
                    ` }).join('')}
                ${state.assets.length === 0 ? `<tr><td colspan="8" style="text-align:center; padding:2rem; color:var(--text-muted)">${t('empty.assets')}</td></tr>` : ''}
            </tbody>
        </table>
    </div>
`,
    Persons: () => {
        const sortedPersons = getSortedPersons();

        return `
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem">
            <h2>${t('people.title')}</h2>
            <div style="display:flex; gap:1rem; align-items:center">
                <div class="search-box">
                    <input type="text" id="personSearch" placeholder="${t('placeholder.search.people')}" value="${state.search.persons}" oninput="handleSearchInput('persons', this)">
                </div>
                 <select class="glass-select" onchange="setPersonSort(this.value)">
                    <option value="firstName" ${state.personSort.field === 'firstName' ? 'selected' : ''}>${t('sort.fname')}</option>
                    <option value="lastName" ${state.personSort.field === 'lastName' ? 'selected' : ''}>${t('sort.lname')}</option>
                    <option value="role" ${state.personSort.field === 'role' ? 'selected' : ''}>${t('sort.role')}</option>
                    <option value="email" ${state.personSort.field === 'email' ? 'selected' : ''}>${t('sort.email')}</option>
                </select>
                ${state.user.role === 'ADMIN' ? `<button class="btn-primary" onclick="openCreatePersonModal()">${t('people.new')}</button>` : ''}
            </div>
        </div>
    <div class="card table-container">
        <table>
            <thead>
                <tr>
                    <th onclick="setPersonSort('firstName')" style="cursor:pointer">${t('table.name')} ${state.personSort.field === 'firstName' ? '*' : ''}</th>
                    <th onclick="setPersonSort('email')" style="cursor:pointer">${t('login.email')} ${state.personSort.field === 'email' ? '*' : ''}</th>
                    <th onclick="setPersonSort('role')" style="cursor:pointer">${t('table.role')} ${state.personSort.field === 'role' ? '*' : ''}</th>
                    <th>${t('table.status')}</th>
                    <th>${t('table.pNumber')}</th>
                    <th>${t('table.actions')}</th>
                </tr>
            </thead>
            <tbody>
                ${sortedPersons.map(p => `
                        <tr style="opacity: ${p.active ? 1 : 0.5}">
                            <td>${p.firstName} ${p.lastName}</td>
                            <td>${p.email}</td>
                            <td><span class="badge" style="background:rgba(255,255,255,0.1)">${p.role}</span></td>
                            <td>
                                ${p.active
                ? `<span class="badge available">${t('status.active')}</span>`
                : `<span class="badge lost">${t('status.inactive')}</span>`}
                            </td>
                            <td>${p.personnelNumber}</td>
                            <td>
                                ${(state.user.role === 'ADMIN' && p.email !== 'admin@test.de') ? `
                                    <button class="btn-secondary btn-sm" onclick="openEditPersonModal('${p.uuid}')">${t('btn.edit')}</button>
                                    ${p.active
                    ? `<button class="btn-danger btn-sm" onclick="togglePersonStatus('${p.uuid}', false)">${t('btn.deactivate')}</button>`
                    : `<button class="btn-primary btn-sm" onclick="togglePersonStatus('${p.uuid}', true)">${t('btn.activate')}</button>`
                }
                                    <button class="btn-danger btn-sm" onclick="deletePerson('${p.uuid}')" style="margin-left:0.5rem">${t('btn.delete')}</button>
                                ` : ''}
                            </td>
                        </tr>
                    `).join('')}
            </tbody>
        </table>
    </div>
` },
    Facilities: () => {
        let list = [...state.facilities];
        const term = (state.search.facilities || '').toLowerCase();

        if (term) {
            list = list.filter(f => f.name.toLowerCase().includes(term));
        }

        return `
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem">
            <h2>${t('locations.title')}</h2>
             <div style="display:flex; gap:1rem; align-items:center">
                <div class="search-box">
                    <input type="text" id="locationSearch" placeholder="${t('placeholder.search.locations')}" value="${state.search.facilities}" oninput="handleSearchInput('facilities', this)">
                </div>
                 ${(state.user.role === 'ADMIN' || state.user.role === 'MANAGER') ? `<button class="btn-primary" onclick="openCreateLocationModal()">${t('locations.new')}</button>` : ''}
            </div>
        </div>
    <div class="grid">
        ${list.map(f => `
                <div class="card hover-lift">
                    <h3 style="color:var(--primary); margin-bottom:0.5rem">${f.name}</h3>
                    <p style="color:var(--text-muted); font-size:0.8rem; margin-bottom:1rem">ID: ${f.id}</p>
                    <div style="display:flex; gap:0.5rem">
                        ${(state.user.role === 'ADMIN' || state.user.role === 'MANAGER') ? `
                            <button class="btn-secondary btn-sm" onclick="openEditLocationModal('${f.id}')">${t('btn.edit')}</button>
                            ${state.user.role === 'ADMIN' ? `<button class="btn-danger btn-sm" onclick="deleteLocation('${f.id}')">${t('btn.delete')}</button>` : ''}
                        ` : ''}
                    </div>
                </div>
            `).join('')}
        ${list.length === 0 ? `<p style="color:var(--text-muted); padding:2rem">${t('empty.locations')}</p>` : ''}
    </div>
` },
    AssetTypes: () => `
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem">
            <h2>${t('types.title')}</h2>
            <button class="btn-primary" onclick="openCreateTypeModal()">${t('types.new')}</button>
        </div>
    <div class="card table-container">
        <table>
            <thead>
                <tr>
                    <th>${t('table.name')}</th>
                    <th>${t('table.id')}</th>
                    <th>${t('table.actions')}</th>
                </tr>
            </thead>
            <tbody>
                ${state.assetTypes.map(type => `
                        <tr>
                            <td>${type.name}</td>
                            <td style="font-family:monospace; color:var(--text-muted)">${type.id}</td>
                            <td>
                                <button class="btn-danger btn-sm" onclick="deleteAssetType('${type.id}')">${t('btn.delete')}</button>
                            </td>
                        </tr>
                    `).join('')}
            </tbody>
        </table>
    </div>
`,
    Reservations: () => {
        let list = [...state.reservations];
        const term = (state.search.reservations || '').toLowerCase();

        if (term) {
            list = list.filter(r => {
                const personName = (r.person ? (r.person.firstName + ' ' + r.person.lastName) : '').toLowerCase();
                const locationName = (r.location ? r.location.name : '').toLowerCase();
                return personName.includes(term) || locationName.includes(term);
            });
        }

        // Sorting logic (simple standard sort by date desc default if not set)
        list.sort((a, b) => new Date(b.start) - new Date(a.start));

        return `
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem">
            <h2>${t('bookings.title')}</h2>
             <div style="display:flex; gap:1rem; align-items:center">
                 <div class="search-box">
                    <input type="text" id="reservationSearch" placeholder="${t('placeholder.search.bookings')}" value="${state.search.reservations}" oninput="handleSearchInput('reservations', this)">
                </div>
                ${(state.user.role === 'ADMIN' || state.user.role === 'MANAGER') ? `<button class="btn-primary" onclick="openCreateReservationModal()">${t('bookings.new')}</button>` : ''}
            </div>
        </div>
    <div class="card table-container">
        <table>
            <thead>
                <tr>
                    <th>${t('table.for')}</th>
                    <th>${t('table.location')}</th>
                    <th>${t('table.reason')}</th>
                    <th>${t('table.start')}</th>
                    <th>${t('table.end')}</th>
                    <th>${t('table.actions')}</th>
                </tr>
            </thead>
            <tbody>
                ${list.map(r => `
                        <tr>
                            <td>${r.person ? (r.person.firstName + ' ' + r.person.lastName) : 'Unknown'}</td>
                            <td>${r.location ? r.location.name : 'Unknown'}</td>
                            <td>${r.reason || '-'}</td>
                            <td>${new Date(r.start).toLocaleString()}</td>
                            <td>${new Date(r.end).toLocaleString()}</td>
                            <td>
                                <button class="btn-danger btn-sm" onclick="deleteReservation('${r.uuid}')">${t('btn.cancel.res')}</button>
                            </td>
                        </tr>
                    `).join('')}
                ${list.length === 0 ? `<tr><td colspan="5" style="text-align:center; padding:2rem; color:var(--text-muted)">${t('empty.reservations')}</td></tr>` : ''}
            </tbody>
        </table>
    </div>
` },
};

// --- Actions & Logic ---

async function apiCall(endpoint, options = {}) {
    const token = state.user?.token;
    options.headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }

    try {
        const res = await fetch(`${API_BASE}${endpoint}`, options);
        if (res.status === 204) return null;
        if (!res.ok) {
            const txt = await res.text();
            throw new Error(txt || `Error ${res.status} `);
        }
        // Handle empty bodies (e.g. 200 OK from void controller methods)
        const text = await res.text();
        return text ? JSON.parse(text) : {};
    } catch (e) {
        console.warn(`API Fail: ${endpoint} `, e);
        throw e;
    }
}

// Fetchers
const loadAll = async () => {
    try {
        state.loading = true; render();

        // Robust Loader: Don't fail everything if one endpoint fails
        const results = await Promise.allSettled([
            apiCall('/assets'),
            (state.user.role === 'ADMIN') ? apiCall('/persons') : apiCall('/persons/assignees'),
            apiCall('/locations'),
            apiCall('/reservations'),
            apiCall('/asset-types')
        ]);

        if (results[0].status === 'fulfilled') state.assets = results[0].value;
        if (results[1].status === 'fulfilled') state.persons = results[1].value;
        if (results[2].status === 'fulfilled') state.facilities = results[2].value;
        if (results[3].status === 'fulfilled') state.reservations = results[3].value;
        if (results[4].status === 'fulfilled') state.assetTypes = results[4].value;

        // Log errors but don't crash UI
        results.forEach((r, i) => {
            if (r.status === 'rejected') console.warn(`Failed to load resource ${i} `, r.reason);
        });

    } catch (e) {
        showToast("Partial Data Load Error", "warning");
    } finally {
        state.loading = false; render();
    }
};
window.loadAll = loadAll;

window.navigate = async (view) => {
    state.view = view;
    // Clear search filters on navigation to prevent hidden items
    state.search = { assets: '', persons: '', facilities: '', reservations: '' };

    // Don't show full loading screen on nav, just fetching indicator if needed or skeleton
    // usage of state.loading = true causes full screen refresh which is jarring.
    // Let's keep data if we have it and just background refresh.
    if (state.assets.length === 0) {
        state.loading = true;
        render(); // Early render only if empty
    }
    await loadAll();
};

window.handleLogin = async (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const res = await apiCall('/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) });
        // Assume backend returns { token: "..." }
        // Determine role from token or email if mock
        let role = 'EMPLOYEE';
        if (email.startsWith('admin')) role = 'ADMIN';
        else if (email.startsWith('manager')) role = 'MANAGER';

        state.user = { email, role, token: res.token };
        showToast(`Welcome back, ${role} `, "success");
        navigate('dashboard');
    } catch (e) {
        alert(t('toast.cancel.fail') + ': ' + e.message);
    }
};

window.retireAsset = async (uuid) => {
    if (!confirm("Confirm retire? Asset cannot be used again.")) return;
    try {
        const response = await fetch(`${API_BASE}/assets/${uuid}/retire`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${state.user.token}` }
        });
        if (!response.ok) throw new Error('Failed to retire asset');
        showToast(t('toast.asset.retired'));
        fetchAssets();
    } catch (e) {
        showToast(e.message, true);
    }
};

window.markForFinalReturn = async (uuid) => {
    if (!await showConfirm(t('action.return') + "?")) return;
    try {
        await apiCall(`/assets/${uuid}/mark-for-final-return`, { method: 'POST' });
        showToast(t('toast.asset.return.marked'));
        fetchAssets();
    } catch (e) {
        showToast(e.message, true);
    }
};

window.confirmFinalReturn = async (uuid) => {
    if (!await showConfirm(t('action.return.confirm') + "?")) return;
    try {
        await apiCall(`/assets/${uuid}/confirm-final-return`, { method: 'POST' });
        showToast(t('toast.asset.return.confirmed'));
        fetchAssets();
    } catch (e) {
        showToast(e.message, true);
    }
};

window.renderAssetTypesApp = () => { state.user = null; state.view = 'login'; render(); };

window.showSupportModal = () => {
    Modal.open(`
        <div style="text-align:center; padding:1rem">
            <h2 style="margin-bottom:1.5rem; color:var(--primary)">${t('support.title')}</h2>
            <div style="background:rgba(255,255,255,0.05); padding:1.5rem; border-radius:var(--radius-md); text-align:left; margin-bottom:1.5rem">
                <!-- Amjad -->
                <div style="margin-bottom:1rem">
                    <h4 style="margin-bottom:0.5rem; font-size:1.1rem">Amjad Faham</h4>
                    <p style="color:var(--text-muted); font-size:0.95rem; margin-bottom:0.25rem">📞 +49 176 3209 1991</p>
                </div>
                
                <!-- Khaled -->
                <div>
                    <h4 style="margin-bottom:0.5rem; font-size:1.1rem">Khaled Almasri</h4>
                    <p style="color:var(--text-muted); font-size:0.95rem">📞 +49 176 7649 6689</p>
                </div>

                <!-- Separator -->
                <div style="text-align:center; margin: 1.5rem 0 1rem 0; color:var(--text-muted); font-size:0.9rem; position:relative;">
                    <span style="background:#1e1e1e; padding:0 0.5rem; position:relative; z-index:2">${t('support.or')}</span>
                    <div style="position:absolute; top:50%; left:0; width:100%; height:1px; background:var(--glass-border); z-index:1"></div>
                </div>

                <!-- Email -->
                <div style="text-align:center">
                    <p style="color:var(--text-muted); font-size:0.95rem">✉️ support11@ALFA-bremen.de</p>
                </div>
            </div>
            <button class="btn-primary" onclick="Modal.close()" style="width:100%">${t('btn.close')}</button>
        </div>
    `);
};

// --- Modals & CRUD ---

// Sorting Logic
state.sort = { field: 'name', dir: 'asc' };

window.setSort = (field) => {
    if (state.sort.field === field) {
        state.sort.dir = state.sort.dir === 'asc' ? 'desc' : 'asc';
    } else {
        state.sort = { field, dir: 'asc' };
    }
    render();
};

const getSortedAssets = () => {
    let list = [...state.assets];

    // Filter by Search
    if (state.search.assets) {
        const q = state.search.assets.toLowerCase();
        list = list.filter(a =>
            (a.name && a.name.toLowerCase().includes(q)) ||
            (a.inventoryNumber && a.inventoryNumber.toLowerCase().includes(q)) ||
            (a.type && a.type.name.toLowerCase().includes(q))
        );
    }

    // Filter by Assigned Status
    if (state.filter.assetsAssignedOnly) {
        list = list.filter(a => a.status === 'ASSIGNED');
    }

    return list.sort((a, b) => {
        let valA = a[state.sort.field];
        let valB = b[state.sort.field];

        // Handle type object
        if (state.sort.field === 'type') {
            valA = a.type ? a.type.name : '';
            valB = b.type ? b.type.name : '';
        }

        // Handle string case
        if (typeof valA === 'string') valA = valA.toLowerCase();
        if (typeof valB === 'string') valB = valB.toLowerCase();

        // Handle nulls
        if (!valA) valA = '';
        if (!valB) valB = '';

        if (valA < valB) return state.sort.dir === 'asc' ? -1 : 1;
        if (valA > valB) return state.sort.dir === 'asc' ? 1 : -1;
        return 0;
    });
};

window.handleSearchInput = (type, el) => {
    const val = el.value;
    const cursorPos = el.selectionStart;

    state.search[type] = val;
    render();

    // Restore focus and cursor position
    requestAnimationFrame(() => {
        let id;
        if (type === 'assets') id = 'assetSearch';
        else if (type === 'persons') id = 'personSearch';
        else if (type === 'facilities') id = 'locationSearch';
        else if (type === 'reservations') id = 'reservationSearch';

        const newEl = document.getElementById(id);
        if (newEl) {
            newEl.focus();
            newEl.value = val; // Ensure value is set (though render does it)
            newEl.setSelectionRange(cursorPos, cursorPos);
        }
    }, 0);
};

window.setAssetSort = (field) => {
    state.sort.field = field;
    render();
};

window.setPersonSort = (field) => {
    state.personSort.field = field;
    render();
};

const getSortedPersons = () => {
    let list = [...state.persons];
    const q = state.search.persons.toLowerCase();

    // Filter
    if (q) {
        list = list.filter(p =>
            p.firstName.toLowerCase().includes(q) ||
            p.lastName.toLowerCase().includes(q) ||
            p.email.toLowerCase().includes(q) ||
            (p.personnelNumber && p.personnelNumber.toLowerCase().includes(q))
        );
    }

    // Sort
    return list.sort((a, b) => {
        let valA = a[state.personSort.field];
        let valB = b[state.personSort.field];

        if (state.personSort.field === 'name') {
            // Virtual field for robust name sorting if needed, but firstName is usually fine
            valA = a.firstName + ' ' + a.lastName;
            valB = b.firstName + ' ' + b.lastName;
        }

        if (typeof valA === 'string') valA = valA.toLowerCase();
        if (typeof valB === 'string') valB = valB.toLowerCase();

        if (!valA) valA = '';
        if (!valB) valB = '';

        if (valA < valB) return state.personSort.dir === 'asc' ? -1 : 1;
        if (valA > valB) return state.personSort.dir === 'asc' ? 1 : -1;
        return 0;
    });
};

window.toggleAssignedFilter = (checked) => {
    state.filter.assetsAssignedOnly = checked;
    render();
};

/* Update Views.Assets in next tool call to use getSortedAssets() and add Sort Headers */

// Assets
window.openCreateAssetModal = () => {
    const today = new Date().toISOString().split('T')[0];
    Modal.open(`
    <h3>${t('modal.create.asset')}</h3>
        <form onsubmit="submitCreateAsset(event)">
            <label>${t('input.name')}</label><input id="mName" required>
                <label>${t('input.inv')}</label><input id="mInv" required>
                    <label>${t('input.type')}</label>
                    <select id="mType" required>${state.assetTypes.map(t => `<option value="${t.id}">${t.name}</option>`).join('')}</select>
                    <label>${t('input.location')}</label>
                    <select id="mLoc" required>${state.facilities.map(l => `<option value="${l.id}">${l.name}</option>`).join('')}</select>
                    <label>${t('input.date')}</label><input id="mDate" type="date" value="${today}">
                    <label>${t('input.warranty')}</label><input id="mWarranty" type="date">

                        <hr style="margin:1rem 0; border:0; border-top:1px solid var(--glass-border)">
                            <label style="color:var(--primary)">${t('input.assign')}</label>
                            <select id="mAssign">
                                <option value="">${t('input.assign.none')}</option>
                                ${state.persons.map(p => `<option value="${p.uuid}">${p.firstName} ${p.lastName}</option>`).join('')}
                            </select>

                            <div style="text-align:right"><button type="submit">${t('btn.create')}</button></div>
                        </form>
                        `);
};

window.submitCreateAsset = async (e) => {
    e.preventDefault();
    const assignUuid = document.getElementById('mAssign').value;

    try {
        const newAsset = await apiCall('/assets', {
            method: 'POST',
            body: JSON.stringify({
                name: document.getElementById('mName').value,
                inventoryNumber: document.getElementById('mInv').value,
                type: document.getElementById('mType').value,
                locationUuid: document.getElementById('mLoc').value,
                purchaseDate: document.getElementById('mDate').value || null,
                warrantyEnd: document.getElementById('mWarranty').value || null
            })
        });

        if (assignUuid && newAsset.uuid) {
            try {
                await apiCall(`/assets/${newAsset.uuid}/assign`, {
                    method: 'PATCH',
                    body: JSON.stringify({ personUuid: assignUuid })
                });
                showToast(t('toast.asset.assigned'), "success");
            } catch (assignErr) {
                console.warn("Auto-assign failed", assignErr);
                showToast(t('toast.asset.assign.fail'), "warning");
            }
        } else {
            showToast(t('toast.asset.created'), "success");
        }

        Modal.close();
        navigate('assets');
    } catch (e) { showToast(e.message, "error"); }
};

window.openEditAssetModal = (uuid) => {
    const asset = state.assets.find(a => a.uuid === uuid);
    if (!asset) return;
    Modal.open(`
                        <h3>${t('modal.edit.asset')}</h3>
                        <form onsubmit="submitEditAsset(event, '${uuid}')">
                            <label>${t('input.name')}</label><input id="eName" value="${asset.name}" required>
                                <label>${t('input.inv')}</label><input id="eInv" value="${asset.inventoryNumber}" required>
                                    <label>${t('input.date')}</label><input id="eDate" type="date" value="${asset.purchaseDate || ''}">
                                    <label>${t('input.warranty')}</label><input id="eWarranty" type="date" value="${asset.warrantyEnd || ''}">
                                        <label>${t('input.location')}</label>
                                        <select id="eLoc" required>
                                            ${state.facilities.map(l => `<option value="${l.uuid}" ${asset.location?.uuid === l.uuid ? 'selected' : ''}>${l.name}</option>`).join('')}
                                        </select>
                                        <div style="text-align:right"><button type="submit">${t('btn.update')}</button></div>
                                    </form>
                                    `);
};

window.submitEditAsset = async (e, uuid) => {
    e.preventDefault();
    try {
        // We only update name, inv, location for now. Type usually stays or is complex.
        const body = {
            name: document.getElementById('eName').value,
            inventoryNumber: document.getElementById('eInv').value,
            locationUuid: document.getElementById('eLoc').value,
            purchaseDate: document.getElementById('eDate').value || null,
            warrantyEnd: document.getElementById('eWarranty').value || null
        };
        await apiCall(`/assets/${uuid}`, { method: 'PUT', body: JSON.stringify(body) });
        showToast(t('toast.asset.updated'), "success");
        Modal.close();
        navigate('assets');
    } catch (e) { showToast(e.message, "error"); }
};

window.deleteAsset = async (uuid) => {
    if (!await showConfirm("Delete?")) return;

    // Optimistic Update
    const originalAssets = [...state.assets];
    state.assets = state.assets.filter(a => a.uuid !== uuid);
    render();

    try {
        await apiCall(`/assets/${uuid}`, { method: 'DELETE' });
        showToast(t('toast.deleted'), "info");
        // Background refresh to ensure consistency
        apiCall('/assets').then(res => { state.assets = res; render(); });
    }
    catch (e) {
        showToast(t('toast.delete.fail'), "error");
        state.assets = originalAssets; // Revert
        render();
    }
};

// Persons
window.openCreatePersonModal = () => {
    Modal.open(`
        <h3>${t('modal.create.person')}</h3>
        <form onsubmit="submitCreatePerson(event)">
            <label>${t('sort.fname')}</label><input id="pFirst" required>
            <label>${t('sort.lname')}</label><input id="pLast" required>
            <label>${t('login.email')}</label><input id="pEmail" type="email" required>
            <div style="text-align:right"><button type="submit">${t('btn.create')}</button></div>
        </form>
    `);
};

window.submitCreatePerson = async (e) => {
    e.preventDefault();
    try {
        await apiCall('/persons', {
            method: 'POST',
            body: JSON.stringify({
                firstName: document.getElementById('pFirst').value,
                lastName: document.getElementById('pLast').value,
                email: document.getElementById('pEmail').value,
                personnelNumber: "P-" + Math.floor(Math.random() * 10000), // Auto-gen
                password: "password"
            })
        });
        showToast(t('toast.person.created'), "success");
        Modal.close();
        navigate('persons');
    } catch (e) { showToast(e.message, "error"); }
};

window.openEditPersonModal = (uuid) => {
    const p = state.persons.find(per => per.uuid === uuid);
    Modal.open(`
                                    <h3>${t('modal.edit.person')}</h3>
                                    <form onsubmit="submitEditPerson(event, '${uuid}')">
                                        <label>${t('sort.fname')}</label><input id="eFirst" value="${p.firstName}" required>
                                            <label>${t('sort.lname')}</label><input id="eLast" value="${p.lastName}" required>
                                                <label>${t('login.email')}</label><input id="eEmail" value="${p.email}" required>
                                                    ${state.user.role === 'ADMIN' ? `
            <label>${t('table.role')}</label>
            <select id="eRole" required>
                <option value="ADMIN" ${p.role === 'ADMIN' ? 'selected' : ''}>Admin</option>
                <option value="MANAGER" ${p.role === 'MANAGER' ? 'selected' : ''}>Manager</option>
                <option value="EMPLOYEE" ${p.role === 'EMPLOYEE' ? 'selected' : ''}>Employee</option>
            </select>
            ` : ''}
                                                    <div style="text-align:right"><button type="submit">${t('btn.update')}</button></div>
                                                </form>
                                                `);
};

window.submitEditPerson = async (e, uuid) => {
    e.preventDefault();
    const body = {
        firstName: document.getElementById('eFirst').value,
        lastName: document.getElementById('eLast').value,
        email: document.getElementById('eEmail').value
    };

    // Add role if element exists (only admins see it)
    const roleEl = document.getElementById('eRole');
    if (roleEl) {
        body.role = roleEl.value;
    }

    try {
        const p = state.persons.find(per => per.uuid === uuid);
        const headers = {};
        if (p && p.version !== undefined) {
            headers['If-Match'] = `"${p.version}"`;
        }

        await apiCall(`/persons/${uuid}`, {
            method: 'PUT',
            headers: headers,
            body: JSON.stringify(body)
        });
        showToast(t('toast.person.updated'), "success");
        Modal.close();
        navigate('persons');
    } catch (e) { showToast(e.message, "error"); }
};

window.deletePerson = async (uuid) => {
    if (!await showConfirm("Delete?")) return;
    try { await apiCall(`/persons/${uuid}`, { method: 'DELETE' }); showToast(t('toast.deleted'), "info"); navigate('persons'); }
    catch (e) { showToast(t('toast.delete.fail'), "error"); }
};

window.togglePersonStatus = async (uuid, active) => {
    const action = active ? 'activate' : 'deactivate';
    if (!await showConfirm(`Confirm ${action} user?`)) return;

    try {
        await apiCall(`/persons/${uuid}/${action}`, { method: 'POST' });
        showToast(`User ${action}d successfully`, "success");
        navigate('persons');
    } catch (e) {
        showToast(e.message, "error");
    }
};

// Locations
window.openCreateLocationModal = () => {
    Modal.open(`
        <h3>${t('modal.create.location')}</h3>
        <form onsubmit="submitCreateLocation(event)">
            <label>${t('table.name')}</label><input id="lName" required>
            <div style="text-align:right"><button type="submit">${t('btn.create')}</button></div>
        </form>
    `);
};

window.submitCreateLocation = async (e) => {
    e.preventDefault();
    try {
        await apiCall('/locations', { method: 'POST', body: JSON.stringify({ name: document.getElementById('lName').value }) });
        showToast(t('toast.loc.created'), "success"); Modal.close(); navigate('facilities');
    } catch (e) { showToast(e.message, "error"); }
};

window.openEditLocationModal = (id) => {
    const loc = state.facilities.find(l => l.id == id);
    Modal.open(`
                                                <h3>${t('modal.edit.location')}</h3>
                                                <form onsubmit="submitEditLocation(event, '${id}')">
                                                    <label>${t('table.name')}</label><input id="elName" value="${loc.name}" required>
                                                        <div style="text-align:right"><button type="submit">${t('btn.update')}</button></div>
                                                </form>
                                                `);
};

window.submitEditLocation = async (e, id) => {
    e.preventDefault();
    try {
        await apiCall(`/locations/${id}`, { method: 'PUT', body: JSON.stringify({ name: document.getElementById('elName').value }) });
        showToast(t('toast.loc.updated'), "success"); Modal.close(); navigate('facilities');
    } catch (e) { showToast(e.message, "error"); }
};

window.deleteLocation = async (id) => {
    if (!await showConfirm("Delete Location?")) return;
    try { await apiCall(`/locations/${id}`, { method: 'DELETE' }); showToast(t('toast.deleted'), "info"); navigate('facilities'); }
    catch (e) { showToast(t('toast.delete.fail'), "error"); }
};

// Types
window.openCreateTypeModal = () => {
    Modal.open(`
        <h3>${t('modal.create.type')}</h3>
        <form onsubmit="submitCreateType(event)">
            <label>${t('table.name')}</label><input id="tName" required>
            <div style="text-align:right"><button type="submit">${t('btn.create')}</button></div>
        </form>
    `);
};

window.submitCreateType = async (e) => {
    e.preventDefault();
    try {
        await apiCall('/asset-types', { method: 'POST', body: JSON.stringify({ name: document.getElementById('tName').value }) });
        showToast(t('toast.type.created'), "success"); Modal.close(); navigate('assetTypes');
    } catch (e) { showToast(e.message, "error"); }
};

window.deleteAssetType = async (id) => {
    if (!await showConfirm("Delete Type?")) return;
    try { await apiCall(`/asset-types/${id}`, { method: 'DELETE' }); showToast(t('toast.deleted'), "info"); navigate('assetTypes'); }
    catch (e) { showToast(t('toast.delete.fail'), "error"); }
};

// Reservations
window.openCreateReservationModal = () => {
    Modal.open(`
        <h3>${t('modal.create.booking')}</h3>
        <form onsubmit="submitCreateReservation(event)">
            <label>${t('input.location')}</label>
            <select id="rLoc" required>${state.facilities.map(f => `<option value="${f.uuid}">${f.name}</option>`).join('')}</select>
            <label>${t('people.title')}</label>
            <select id="rPer" required>${state.persons.map(p => `<option value="${p.uuid}">${p.firstName} ${p.lastName}</option>`).join('')}</select>
            <div style="display:grid; grid-template-columns: 1fr 1fr; gap:1rem">
                <div><label>${t('table.start')}</label><input id="rStart" type="datetime-local" required></div>
                <div><label>${t('table.end')}</label><input id="rEnd" type="datetime-local" required></div>
            </div>
            <label>${t('table.reason')}</label><input id="rReason">
             <div style="text-align:right"><button type="submit">${t('bookings.new')}</button></div>
        </form>
    `);
};

window.submitCreateReservation = async (e) => {
    e.preventDefault();
    let start = document.getElementById('rStart').value;
    let end = document.getElementById('rEnd').value;
    if (start.length === 16) start += ':00';
    if (end.length === 16) end += ':00';

    try {
        await apiCall('/reservations', {
            method: 'POST',
            body: JSON.stringify({
                locationUuid: document.getElementById('rLoc').value,
                personUuid: document.getElementById('rPer').value,
                start,
                end,
                reason: document.getElementById('rReason').value
            })
        });
        showToast(t('toast.res.confirmed'), "success"); Modal.close(); navigate('reservations');
    } catch (e) { showToast(e.message, "error"); }
};

window.deleteReservation = async (uuid) => {
    if (!await showConfirm("Cancel Reservation?")) return;
    try { await apiCall(`/reservations/${uuid}`, { method: 'DELETE' }); showToast(t('toast.cancelled'), "info"); navigate('reservations'); }
    catch (e) { showToast(t('toast.cancel.fail'), "error"); }
};

// Assign/Return
// Helper to refresh data
window.reloadAssets = async () => {
    try {
        const res = await apiCall('/assets');
        state.assets = res;
        render(); // Re-render with new data
    } catch (e) { console.error("Reload failed", e); }
};

// Workflow Actions
window.confirmHandover = async (uuid) => {
    try { await apiCall(`/assets/${uuid}/handover`, { method: 'POST' }); showToast(t('toast.handover'), "success"); reloadAssets(); }
    catch (e) { showToast(e.message, "error"); }
};

window.markLost = async (uuid) => {
    if (!await showConfirm("Report this asset as LOST?")) return;
    try { await apiCall(`/assets/${uuid}/mark-lost`, { method: 'POST' }); showToast(t('toast.lost.reported'), "warning"); reloadAssets(); }
    catch (e) { showToast(e.message, "error"); }
};

window.confirmLost = async (uuid) => {
    if (!await showConfirm("Confirm asset is permanently lost?")) return;
    try { await apiCall(`/assets/${uuid}/confirm-lost`, { method: 'POST' }); showToast(t('toast.lost.confirmed'), "success"); reloadAssets(); }
    catch (e) { showToast(e.message, "error"); }
};

window.startMaintenance = async (uuid) => {
    try { await apiCall(`/assets/${uuid}/maintenance/start`, { method: 'POST' }); showToast(t('toast.maint.start'), "info"); reloadAssets(); }
    catch (e) { showToast(e.message, "error"); }
};

window.finishMaintenance = async (uuid) => {
    try { await apiCall(`/assets/${uuid}/maintenance/finish`, { method: 'POST' }); showToast("Maintenance Finished", "success"); reloadAssets(); }
    catch (e) { showToast(e.message, "error"); }
};

// Dropdown Utils
window.toggleActionMenu = (uuid, event) => {
    event.stopPropagation();
    // Close others
    document.querySelectorAll('.action-dropdown.show').forEach(el => {
        if (el.id !== `action-menu-${uuid}`) el.classList.remove('show');
    });

    const menu = document.getElementById(`action-menu-${uuid}`);
    if (menu) {
        menu.classList.toggle('show');
    }
};

window.closeAllActionMenus = () => {
    document.querySelectorAll('.action-dropdown.show').forEach(el => el.classList.remove('show'));
};

window.addEventListener('click', () => window.closeAllActionMenus());


// Action Handler (Legacy/Fallback)
window.handleAssetAction = (select, uuid) => {
    const action = select.value;
    select.value = ""; // Reset

    switch (action) {
        case 'edit': openEditAssetModal(uuid); break;
        case 'delete': deleteAsset(uuid); break;
        case 'assign': openAssignModal(uuid); break;
        case 'handover': confirmHandover(uuid); break;
        case 'confirmLost': confirmLost(uuid); break;
        case 'return': openReturnModal(uuid); break;
        case 'startMaint': startMaintenance(uuid); break;
        case 'finishMaint': finishMaintenance(uuid); break;
        case 'reportDefect': reportDefect(uuid); break;
        case 'markLost': markLost(uuid); break;
    }
};

window.reportDefect = async (uuid) => {
    if (!await showConfirm("Report defect? The asset will be marked for repair return.")) return;
    try {
        await apiCall(`/assets/${uuid}/mark-for-repair-return`, { method: 'POST' });
        showToast("Defect Reported", "success");
        reloadAssets();
    }
    catch (e) { showToast(e.message, "error"); }
};

window.confirmRepairReturn = async (uuid) => {
    if (!await showConfirm("Confirm return for repair?")) return;
    try { await apiCall(`/assets/${uuid}/confirm-repair-return`, { method: 'POST' }); showToast("Return Confirmed", "success"); reloadAssets(); }
    catch (e) { showToast(e.message, "error"); }
};

window.confirmFinalReturn = async (uuid) => {
    if (!await showConfirm(t('action.return.confirm') + "?")) return;
    try {
        await apiCall(`/assets/${uuid}/confirm-final-return`, { method: 'POST' });
        showToast(t('toast.asset.return.confirmed'), "success");
        reloadAssets();
    } catch (e) {
        showToast(e.message, "error");
    }
};

window.restock = async (uuid) => {
    try { await apiCall(`/assets/${uuid}/restock`, { method: 'POST' }); showToast("Restocked", "success"); reloadAssets(); }
    catch (e) { showToast(e.message, "error"); }
};

window.openReturnModal = async (uuid) => {
    if (await showConfirm("Confirm return of this asset?")) {
        try { await apiCall(`/assets/${uuid}/return`, { method: 'POST' }); showToast("Returned", "success"); reloadAssets(); }
        catch (e) { showToast(e.message, "error"); }
    }
}

window.markForFinalReturn = async (uuid) => {
    if (!await showConfirm(t('action.return.request') + "?")) return;
    try {
        await apiCall(`/assets/${uuid}/mark-for-final-return`, { method: 'POST' });
        showToast(t('toast.asset.return.marked'), "success");
        reloadAssets();
    } catch (e) {
        // Handle race condition gracefully
        if (e.message.includes("Asset is not assigned")) {
            // Maybe it's already waiting? Reload to check
            reloadAssets();
        } else {
            showToast(e.message, "error");
        }
    }
};

window.openAssignModal = (uuid) => {
    window.currentAssetUuid = uuid;
    Modal.open(`
                                                <h3>Assign Asset</h3>
                                                <label>Select Person</label>
                                                <select id="assignPer" required>
                                                    <option value="">-- Choose --</option>
                                                    ${state.persons.map(p => `<option value="${p.uuid}">${p.firstName} ${p.lastName}</option>`).join('')}
                                                </select>
                                                <label style="margin-top:1rem; display:block">${t('input.return.deadline')}</label>
                                                <input type="datetime-local" id="assignDeadline">
                                                <button class="btn-primary" style="margin-top:1rem; width:100%" onclick="submitAssignment()">Confirm Assignment</button>
                                                `);
};

window.openExtendDeadlineModal = (uuid) => {
    window.currentAssetUuid = uuid;
    Modal.open(`
        <h3>${t('modal.extend.deadline')}</h3>
        <label>${t('input.return.deadline')}</label>
        <input type="datetime-local" id="extendDate" required>
        <div style="text-align:right; margin-top:1rem">
            <button class="btn-primary" onclick="submitExtendDeadline()">${t('btn.save')}</button>
        </div>
    `);
};

window.submitExtendDeadline = async () => {
    let date = document.getElementById('extendDate').value;
    if (!date) return showToast("Select a date", "warning");
    if (date.length === 16) date += ':00';

    try {
        await apiCall(`/assets/${window.currentAssetUuid}/extend-deadline`, {
            method: 'PATCH',
            body: JSON.stringify({ deadline: date })
        });
        showToast("Deadline Extended", "success");
        Modal.close();
        reloadAssets();
    } catch (e) { showToast(e.message, "error"); }
};

window.submitAssignment = async () => {
    const pUuid = document.getElementById('assignPer').value;
    let deadline = document.getElementById('assignDeadline').value;
    if (!pUuid) return showToast("Select a person", "warning");

    if (deadline && deadline.length === 16) deadline += ':00'; // Append seconds if missing

    try {
        await apiCall(`/assets/${window.currentAssetUuid}/assign`, {
            method: 'PATCH',
            body: JSON.stringify({
                personUuid: pUuid,
                returnDeadline: deadline || null
            })
        });
        showToast("Assigned!", "success"); Modal.close(); navigate('assets');
    } catch (e) { showToast(e.message, "error"); }
};

window.returnAsset = async (uuid) => {
    if (!await showConfirm("Return asset to stock?")) return;
    try {
        await apiCall(`/assets/${uuid}/return`, { method: 'PATCH' });
        showToast("Returned", "success"); navigate('assets');
    } catch (e) { showToast("Failed to return", "error"); }
};

// Initial Render
render();

// --- FIXED / APPENDED FUNCTIONS ---

window.retireAsset = async (uuid) => {
    if (!await showConfirm(t('action.retire') + "?")) return;
    try {
        await apiCall(`/assets/${uuid}/retire`, { method: 'POST' });
        showToast(t('toast.asset.retired'), "success");
        reloadAssets();
    } catch (e) {
        showToast(e.message, true);
    }
};

window.logout = () => {
    localStorage.removeItem('bokerfi_user');
    state.user = null;
    state.error = null;
    state.view = 'login';
    render();
};

window.handleLogin = async (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        // 1. Login to get token
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            const text = await response.text();
            let msg = t('toast.login.fail');

            if (text) {
                try {
                    const data = JSON.parse(text);
                    if (data.error === 'USER_DEACTIVATED') {
                        msg = t('login.fail.deactivated');
                    } else {
                        msg = data.message || data.error || text;
                    }
                } catch (e) {
                    msg = text;
                }
            }
            throw new Error(msg);
        }

        const data = await response.json();
        const token = data.token;

        // 2. Fetch User Details (Role) using the new token
        const meResponse = await fetch(`${API_BASE}/auth/me`, {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!meResponse.ok) {
            throw new Error("Failed to fetch user profile");
        }

        const meData = await meResponse.json();

        // Backend returns PersonDTO with 'role' (enum string)
        state.user = {
            email: meData.email,
            role: meData.role,
            token: token
        };

        localStorage.setItem('bokerfi_user', JSON.stringify(state.user));
        state.view = 'dashboard';

        // Trigger data load
        if (window.loadAll) window.loadAll();
        else render();

    } catch (err) {
        state.error = err.message;
        render();
    }
};
