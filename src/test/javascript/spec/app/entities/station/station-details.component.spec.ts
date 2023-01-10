/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import StationDetailComponent from '@/entities/station/station-details.vue';
import StationClass from '@/entities/station/station-details.component';
import StationService from '@/entities/station/station.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Station Management Detail Component', () => {
    let wrapper: Wrapper<StationClass>;
    let comp: StationClass;
    let stationServiceStub: SinonStubbedInstance<StationService>;

    beforeEach(() => {
      stationServiceStub = sinon.createStubInstance<StationService>(StationService);

      wrapper = shallowMount<StationClass>(StationDetailComponent, {
        store,
        localVue,
        router,
        provide: { stationService: () => stationServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundStation = { id: 123 };
        stationServiceStub.find.resolves(foundStation);

        // WHEN
        comp.retrieveStation(123);
        await comp.$nextTick();

        // THEN
        expect(comp.station).toBe(foundStation);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundStation = { id: 123 };
        stationServiceStub.find.resolves(foundStation);

        // WHEN
        comp.beforeRouteEnter({ params: { stationId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.station).toBe(foundStation);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
