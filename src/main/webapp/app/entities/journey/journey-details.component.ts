import { Component, Vue, Inject } from 'vue-property-decorator';

import { IJourney } from '@/shared/model/journey.model';
import JourneyService from './journey.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class JourneyDetails extends Vue {
  @Inject('journeyService') private journeyService: () => JourneyService;
  @Inject('alertService') private alertService: () => AlertService;

  public journey: IJourney = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.journeyId) {
        vm.retrieveJourney(to.params.journeyId);
      }
    });
  }

  public retrieveJourney(journeyId) {
    this.journeyService()
      .find(journeyId)
      .then(res => {
        this.journey = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
