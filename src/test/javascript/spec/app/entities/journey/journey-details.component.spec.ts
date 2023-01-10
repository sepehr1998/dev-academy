/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import JourneyDetailComponent from '@/entities/journey/journey-details.vue';
import JourneyClass from '@/entities/journey/journey-details.component';
import JourneyService from '@/entities/journey/journey.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Journey Management Detail Component', () => {
    let wrapper: Wrapper<JourneyClass>;
    let comp: JourneyClass;
    let journeyServiceStub: SinonStubbedInstance<JourneyService>;

    beforeEach(() => {
      journeyServiceStub = sinon.createStubInstance<JourneyService>(JourneyService);

      wrapper = shallowMount<JourneyClass>(JourneyDetailComponent, {
        store,
        localVue,
        router,
        provide: { journeyService: () => journeyServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundJourney = { id: 123 };
        journeyServiceStub.find.resolves(foundJourney);

        // WHEN
        comp.retrieveJourney(123);
        await comp.$nextTick();

        // THEN
        expect(comp.journey).toBe(foundJourney);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundJourney = { id: 123 };
        journeyServiceStub.find.resolves(foundJourney);

        // WHEN
        comp.beforeRouteEnter({ params: { journeyId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.journey).toBe(foundJourney);
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
