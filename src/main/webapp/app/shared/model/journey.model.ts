import { IStation } from '@/shared/model/station.model';

export interface IJourney {
  id?: number;
  distance?: number;
  duration?: string;
  departureStation?: IStation;
  returnStation?: IStation;
}

export class Journey implements IJourney {
  constructor(
    public id?: number,
    public distance?: number,
    public duration?: string,
    public departureStation?: IStation,
    public returnStation?: IStation
  ) {}
}
