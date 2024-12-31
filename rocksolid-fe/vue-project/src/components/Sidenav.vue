<script>
import { navBarData } from '@/data/navbarData.js';

export default {
  name: "SideNav",
  props: {
    layout: {
      type: String,
      required: true,
    },
  },
  computed: {
    filteredNavBarData() {
      return navBarData.filter(item => item.layout === this.layout);
    },
  },
};
</script>

<template>
  <div class="sidenav">
    <div class="SideMenuHeader">
      <span class="SideMenuHeader-name">
        RockSolid
      </span>
    </div>
    <ul class="sidenav-nav">
      <div class="SideMenuTitle">
        <span class="SideMenuTitle-text">Menu</span>
      </div>
      <li v-for="data in filteredNavBarData" :key="data.label">
        <router-link
            :to="data.routerLink"
            class="sidenav-nav-link"
            active-class="active"
            exact
        >
          <span class="sidenav-link-icon">
            <v-icon size="small">{{ data.icon }}</v-icon> <!-- Používanie Vuetify ikon -->
          </span>
          <span class="sidenav-link-text">{{ data.label }}</span>
        </router-link>
      </li>
      <li>
        <div class="edit-profile">
          <router-link
              to="/user"
              class="sidenav-nav-link"
              active-class="active"
              exact
          >
            <span class="sidenav-link-icon">
              <v-icon>mdi-account</v-icon>
            </span>
          </router-link>
        </div>
      </li>
    </ul>
  </div>
</template>

<style scoped>

.edit-profile {
  position: absolute;
  bottom: 1rem;
  right: 1rem;
}

.sidenav {
  background: #FFFFFF;
  transition: all 0.5s ease;
  position: fixed;
  z-index: 1;
  top: 0;
  width: 11.5rem;
  height: 100vh;
  border-right: 1px solid #d8d8f0;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
}

.SideMenuHeader {
  display: flex;
  align-items: center;
  width: 100%;
  padding-top: 6px;
  padding-bottom: 4px;
}

.SideMenuHeader-name {
  margin-left: 0.5rem;
  font-weight: bold;
  font-family: 'Inter', sans-serif;
  color: black;
}

.SideMenuTitle {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 6px;
}

.SideMenuTitle-text {
  display: inline-flex;
  overflow: hidden;
  align-items: center;
  color: inherit;
  text-decoration: none;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 12px;
  font-family: 'Axiforma', sans-serif;
  color: #aaa8bd;
}


.sidenav-nav {
  list-style: none;
  padding: 0;
  margin: 2.5rem 0 0;
  display: flex;
  flex-direction: column;
  width: 100%;
}

.sidenav-nav-link {
  display: flex;
  align-items: center; /* Zarovná ikonu a text na stred */
  color: #aaa8bd;
  text-decoration: none;
  border-radius: 5px;
  transition: all 0.3s ease;
  padding: 6px 0px;

  &:hover {
    color: #333638;
    background-color: #B2E0D9;

    .sidenav-link-icon {
      color: #333638;
    }

    .sidenav-link-text {
      color: #333638;
    }
  }

  &.active {
    color: #333638;

    .sidenav-link-icon {
      color: #333638;
    }

    .sidenav-link-text {
      color: #333638;
    }
  }
}

.sidenav-link-icon {
  min-width: 1.4rem;
  text-align: center;
  margin-right: 2px;
  line-height: 1;
  color: #aaa8bd;
}

.sidenav-link-icon i {
  position: relative;
  top: -1px;
  left: 1px;
}

.sidenav-link-text {
  font-weight: 550;
  font-family: 'Inter', sans-serif;
  text-align: left;
  color: #aaa8bd;
  line-height: 1;
  vertical-align: baseline;
}
</style>
