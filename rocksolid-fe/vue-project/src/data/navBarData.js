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
        label: 'Active Conferences',
        role: ["STUDENT", "REVIEWER"]
    },
    {
        routerLink: '/web/review-article',
        icon: 'mdi-file-document-multiple-outline',
        label: 'Review Article',
        role: ["REVIEWER"]
    },
    {
        routerLink: '/admin/users',
        icon: 'mdi-account-multiple-outline',
        label: 'Users',
        role: ["ADMIN"]
    },
    {
        routerLink: '/admin/conference',
        icon: 'mdi-account-multiple-outline',
        label: 'Conferences',
        role: ["ADMIN"]
    },
    {
        routerLink: '/admin/articles',
        icon: 'mdi-account-multiple-outline',
        label: 'Articles',
        role: ["ADMIN"]
    },
    {
        routerLink: '/web/history-articles',
        icon: 'mdi-file-clock-outline',
        label: 'Article History',
        role: ["STUDENT", "REVIEWER"]
    },
];