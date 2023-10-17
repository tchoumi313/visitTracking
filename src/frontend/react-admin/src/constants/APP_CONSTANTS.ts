import { UserResponse } from "../generated";
import { USER_LOCAL_STORAGE_KEY } from "./LOCAL_STORAGE";

export enum ROLE {
    admin = "ADMIN",
    user = "USERS",
    recep = "RECEP"
}

export const SEXE: { name: string, value: string }[] = [
    { name: "Choisir..", value: "null" },
    { name: "Masculin", value: "masculin" },
    { name: "Féminin", value: "féminin" },
]

export const UPLOAD_IMAGE = "UPLOAD_IMAGE";

export enum MODAL_MODE {
    create = "CREATE",
    update = "UPDATE",
    view = "VIEW",
}

export enum TYPE_VISITE {
    ordinaire = "ordinaire",
    rendez_vous = "rendez-vous"
}

export enum STATUS_RDV {
    draft = "En Attente",
    pendind = "En Cours",
    passed = "Passe"
}

export interface Time {
    hour: number,
    min: number, 
}

export const listeRoles = (current: UserResponse): string[] => {
  let liste: string[] = [];
  const roles = current.roles;
  roles!.forEach(role => {
    liste.push(role.nom!);
  });
  return liste;
} 

export const currentUser : UserResponse = JSON.parse(localStorage.getItem(USER_LOCAL_STORAGE_KEY) || '{}');