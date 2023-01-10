import { Component, Vue, Inject } from 'vue-property-decorator';

import { decimal, required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import StationService from '@/entities/station/station.service';
import { IStation } from '@/shared/model/station.model';

import { IJourney, Journey } from '@/shared/model/journey.model';
import JourneyService from './journey.service';

const validations: any = {
  journey: {
    distance: {
      required,
      decimal,
    },
    duration: {
      required,
    },
    departureStation: {
      required,
    },
    returnStation: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class JourneyUpdate extends Vue {
  @Inject('journeyService') private journeyService: () => JourneyService;
  @Inject('alertService') private alertService: () => AlertService;

  public journey: IJourney = new Journey();

  @Inject('stationService') private stationService: () => StationService;

  public stations: IStation[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.journeyId) {
        vm.retrieveJourney(to.params.journeyId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.journey.id) {
      this.journeyService()
        .update(this.journey)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Journey is updated with identifier ' + param.id;
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.journeyService()
        .create(this.journey)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Journey is created with identifier ' + param.id;
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveJourney(journeyId): void {
    this.journeyService()
      .find(journeyId)
      .then(res => {
        this.journey = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.stationService()
      .retrieve()
      .then(res => {
        this.stations = res.data;
      });
  }
}
