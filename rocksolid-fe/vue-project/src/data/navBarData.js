export const navBarData = [
    {
        routerLink: '/web/home',
        icon: 'mdi-home-outline',
        label: 'Home',
        role: ["STUDENT", "REVIEWER"]
    },
    {
        routerLink: '/web/active-conferences',
        icon: 'mdi-presentation',
        label: 'Aktívne konferencie',
        role: ["STUDENT", "REVIEWER"]
    },
    {
        routerLink: '/web/review-article',
        icon: 'mdi-file-document-multiple-outline',
        label: 'Pridelené práce',
        role: ["REVIEWER"]
    },
    {
        routerLink: '/admin/users',
        icon: 'mdi-account-multiple-outline',
        label: 'Zoznam používateľov',
        role: ["ADMIN"]
    },
    {
        routerLink: '/admin/conference',
        icon: 'mdi mdi-calendar-multiselect',
        label: 'Konferencie',
        role: ["ADMIN"]
    },
    {
        routerLink: '/admin/articles',
        icon: 'mdi mdi-file',
        label: 'Odovzdané práce',
        role: ["ADMIN"]
    },
    {
        routerLink: '/admin/categories',
        icon: 'mdi-view-list',
        label: 'Kategórie',
        role: ["ADMIN"]
    },
    {
        routerLink: '/web/history-articles',
        icon: 'mdi-file-clock-outline',
        label: 'História prác',
        role: ["STUDENT", "REVIEWER"]
    },
];