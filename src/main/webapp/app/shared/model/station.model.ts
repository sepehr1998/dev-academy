export interface IStation {
  id?: number;
  name?: string;
  address?: string | null;
  startedFrom?: number | null;
  endingIn?: number | null;
}

export class Station implements IStation {
  constructor(
    public id?: number,
    public name?: string,
    public address?: string | null,
    public startedFrom?: number | null,
    public endingIn?: number | null
  ) {}
}
