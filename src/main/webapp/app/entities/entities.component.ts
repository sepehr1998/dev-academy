import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import StationService from './station/station.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('stationService') private stationService = () => new StationService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
