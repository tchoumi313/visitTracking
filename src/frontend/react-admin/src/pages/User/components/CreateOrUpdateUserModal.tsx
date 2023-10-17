import { useEffect, useState } from "react";
import { MODAL_MODE, SEXE } from "../../../constants/APP_CONSTANTS";
import { EditIcon, NewIcon } from "../../../constants/Icon";
import { UserRequest, RoleResponse, UserResponse, BureauResponse } from "../../../generated/models";
import { TOKEN_LOCAL_STORAGE_KEY } from "../../../constants/LOCAL_STORAGE";
import { BureauControllerApi, RoleControllerApi, UserControllerApi } from "../../../generated";
import { useSelector } from "react-redux";
import { ReduxProps } from "../../../redux/configureStore";
import Indicator from "../../Authentication/components/Indicator";
import CustomSelectBureau from "../../../components/CustomSelects/CustomSelectBureau/CustomSelectBureau";
import CustomSelectMultiRoles from "../../../components/CustomSelects/CustomSelectRoles/CustomSelectMultiRoles";

interface ModalProps {
  mode: MODAL_MODE,
  title: string,
  onClose: () => void,
  refresh?: () => void,
  item?: UserResponse | null,

  setShowSuccessNotif: (value: boolean) => void,
  setSuccessNotifMessage: (value: string) => void,
  setSuccessNotifDescription: (value: string | null) => void,

  setShowWarning: (value: boolean) => void,
  setWarningMessage: (value: string) => void,
  setWarningNotifDescription: (value: string | null) => void,
}

const CreateOrUpdateUserModal: React.FC<ModalProps> = (props) => {

  const state = useSelector((state: ReduxProps) => state);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const [formData, setFormData] = useState({
    username: props.item ? props.item.username : '',
    nom: props.item ? props.item.nom : '',
    prenom: props.item ? props.item.prenom : '',
    email: props.item ? props.item.email : '',
    sexe: props.item ? props.item.sexe : '',
    tel: props.item ? props.item.tel : '',
    poste: props.item ? props.item.poste : '',
    dateNais: new Date(props.item?.dateNais!),
    bureau: props.item ? props.item.bureau : {},
    roles: props.item ? props.item.roles : [],
  });
  const validDate: boolean = formData.dateNais.toString() != "Invalid Date"
  
  const [bureaux, setBureaux] = useState<BureauResponse[]>([]);
  const [roles, setRoles] = useState<RoleResponse[]>([]);

  const handleTypingInput = (keyword: string) => {
    if (keyword.length > 0) {
      const token: string = localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY)!;
      const bureauxApi = new BureauControllerApi({
        ...state.environment,
        accessToken: token,
      });

      bureauxApi
        .records4(keyword)
        .then((response) => {
          if (response.status === 200) {
            setBureaux(response.data);
          }
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => { });
    }
  };

  const handleOptionSelect = (item: BureauResponse) => {
    setFormData((prevValues) => ({
      ...prevValues,
      bureau: item,
    }));
  };

  const handleTypingRole = (keyword: string) => {
    if (keyword.length > 0) {
      const token: string = localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY)!;
      const rolesApi = new RoleControllerApi({
        ...state.environment,
        accessToken: token,
      });

      rolesApi
        .records3(keyword)
        .then((response) => {
          if (response && response.data) {
            if (response.status === 200) {
              setRoles(response.data);
            }
          }
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => { });
    }
  };

  const handleRoleSelect = (option: RoleResponse[]) => {
    setFormData((prevValues) => ({
      ...prevValues,
      roles: option,
    }));
  };

  const isEmpty = !formData.username || !formData.nom || !formData.prenom || !formData.email || !formData.sexe || !formData.tel || !formData.poste || !formData.dateNais || !formData.roles || !formData.bureau

  const handleInputChange = (e: any) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape") { props.onClose(); }
    };
    document.addEventListener("keydown", handleKeyDown);
    return () => { document.removeEventListener("keydown", handleKeyDown); };
  }, [props]);

  const handleCreate = (_event: any) => {
    _event.preventDefault() // stopper la soumissoin par defaut du formulaire...

    const token: string = localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY)!;
    const usersApi = new UserControllerApi({ ...state.environment, accessToken: token });

    setIsLoading(true)

    const apiParams: UserRequest = {
      ...formData
    }

    usersApi.create2(apiParams)
      .then((response) => {
        if (response && response.data) {
          if (response.status === 201) {
            props.onClose()
            if (props.refresh) props.refresh()

            // notification
            props.setSuccessNotifMessage('Succes')
            props.setSuccessNotifDescription('Un nouveau user viens d\'etre rajoute au catalogue avec succes ! ')
            props.setShowSuccessNotif(true)
          }
        }
      })
      .catch((error) => {
        console.log(error);
        if (error.response && error.response.status === 400) {
          console.log(error.response.data)
        }
        alert(error?.response?.data?.message)
      })
      .finally(() => {
        setIsLoading(false)

        // notification
        setTimeout(() => {
          props.setShowSuccessNotif(false)
        }, 3000);
      });
  }

  const handleUpdate = (_event: any) => {
    _event.preventDefault() // stopper la soumissoin par defaut du formulaire...

    const token: string = localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY)!;
    const usersApi = new UserControllerApi({ ...state.environment, accessToken: token });

    setIsLoading(true)

    const apiParams: UserRequest = {
      ...formData
    }

    usersApi.update2(apiParams, props.item?.id!)
      .then((response) => {
        if (response && response.data) {
          console.log(response.data);

          if (response.status === 202) {
            props.onClose()
            if (props.refresh) props.refresh()

            // notification
            props.setSuccessNotifMessage('Succes')
            props.setSuccessNotifDescription('Cet employe a ete correctement mise a jour, veuillez consulter le catalogue !')
            props.setShowSuccessNotif(true)
          }
        }
      })
      .catch((err) => {
        console.log(err)
        if (err.response && err.response.status === 400) {
          // props.setShowWarning(true);
          // props.setWarningMessage('Mauvaise');
          // props.setWarningNotifDescription(JSON.stringify(err.response.data));
          alert(err.response)
        } else {
          if (err.response.status === 403) {
            props.onClose();
            props.setShowWarning(true);
            props.setWarningMessage("Probleme de token");
            props.setWarningNotifDescription("Votre token n\'est plus valide. Vous allez etre deconnecte")
          }
        }
      })
      .finally(() => {
        setIsLoading(false)

        // notification
        setTimeout(() => {
          props.setShowSuccessNotif(false)
        }, 3000);
      });

  }

  return (
    <div
      id="authentication-modal"
      className="authentication-modal fixed inset-0 z-50 flex items-center justify-center overflow-auto"
      onClick={props.onClose}
    >
      <div
        className="top-modal-animation relative mx-auto w-full max-w-3xl items-center justify-center px-4 sm:top-0 sm:max-h-96 sm:px-6  lg:top-0 lg:ml-[25%] lg:px-8 max-sm:mt-[150%] max-xsm:mt-[200%]"
        onClick={(event) => event.stopPropagation()}
      >
        {/* Contact Form */}
        <div className="rounded-sm border border-stroke bg-white shadow-default dark:border-strokedark dark:bg-boxdark">

          <div className="p-[2rem]">
            <button onClick={props.onClose} className="flex">
              <svg
                aria-hidden="true"
                className="close-icon"
                fill="currentColor"
                viewBox="0 0 20 20"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  fillRule="evenodd"
                  d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                  clipRule="evenodd"
                />
              </svg>
              <span className="sr-only">Close modal</span>
            </button>
            <div className="border-b border-stroke py-4 px-6.5 dark:border-strokedark -mt-6 pb-1">
              <h3 className="mx-5 mb-1 text-size-[1.25rem] font-medium text-black dark:text-white">
                {props.title}
              </h3>
            </div>
            <form
              action="#"
              encType="multipart/form-data"
              className="modal-form"
            >
              <div>
                {/* row 1 nom, prenom, username*/}
                <div className="mb-4.5 flex flex-col gap-6 xl:flex-row">
                  <div className="w-full xl:w-1/3">
                    <label className="mb-2.5 block text-black dark:text-white">
                      Nom <span className="text-meta-1">*</span>
                    </label>
                    <input
                      name="nom"
                      value={formData.nom}
                      onChange={handleInputChange}
                      required
                      type="text"
                      placeholder="Entrer le nom"
                      className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                    />
                  </div>

                  <div className="w-full xl:w-1/3">
                    <label className="mb-2.5 block text-black dark:text-white">
                      Prenom<span className="text-meta-1">*</span>
                    </label>
                    <input
                      name="prenom"
                      value={formData.prenom}
                      onChange={handleInputChange}
                      required
                      type="text"
                      placeholder="Entrer le prenom"
                      className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                    />
                  </div>

                  <div className="w-full xl:w-1/2">
                    <label className="mb-2.5 block text-black dark:text-white">
                      Username <span className="text-meta-1">*</span>
                    </label>
                    <input
                      name="username"
                      value={formData.username}
                      onChange={handleInputChange}
                      required
                      type="text"
                      placeholder="Entrer le username de l'employe"
                      className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                    />
                  </div>
                </div>

                {/* row 2  dateNais, sexe, email*/}
                <div className="mb-4.5 flex flex-col gap-6 xl:flex-row">
                  <div className="w-full xl:w-1/3 ">
                    <label className="mb-3 block text-black dark:text-white">
                      Date de naissance <span className="text-meta-1">*</span>
                    </label>
                    <div className="relative">
                      <input
                        name="dateNais"
                        value={validDate ? formData.dateNais instanceof Date ? formData.dateNais.toISOString().substring(0, 10) : formData.dateNais : ''}
                        onChange={handleInputChange}
                        type="date"
                        className="custom-input-date custom-input-date-1 w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                      />
                    </div>
                  </div>
                  <div className="w-full xl:w-1/3">
                    <label className="mb-2.5 block text-black dark:text-white">
                      Sexe <span className="text-meta-1">*</span>
                    </label>
                    <select
                      name="sexe"
                      value={formData.sexe}
                      onChange={handleInputChange}
                      className="relative z-20 w-full appearance-none rounded border border-stroke bg-transparent py-3 px-5 outline-none transition focus:border-primary active:border-primary dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                    >
                      {SEXE.map((option, index) => (
                        <option key={index} value={option.value}>
                          {option.name}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="w-full xl:w-1/2">
                    <label className="mb-2.5 block text-black dark:text-white">
                      Email <span className="text-meta-1">*</span>
                    </label>
                    <input
                      name="email"
                      value={formData.email}
                      onChange={handleInputChange}
                      required
                      type="email"
                      placeholder="Entrer l'adresse email"
                      className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                    />
                  </div>
                </div>

                {/* row 3 telephone, poste*/}

                <div className="mb-4.5 flex flex-col gap-6 xl:flex-row">
                  <div className="w-full xl:w-1/2">
                    <label className="mb-2.5 block text-black dark:text-white">
                      Telephone <span className="text-meta-1">*</span>
                    </label>
                    <input
                      name="tel"
                      value={formData.tel}
                      onChange={handleInputChange}
                      required
                      type="text"
                      placeholder="Entrer le numero de telephone"
                      className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                    />
                  </div>
                  <div className="w-full xl:w-1/2">
                    <label className="mb-2.5 block text-black dark:text-white">
                      Poste <span className="text-meta-1">*</span>
                    </label>
                    <input
                      name="poste"
                      value={formData.poste}
                      onChange={handleInputChange}
                      required
                      type="text"
                      placeholder="Entrer le poste de l'employe"
                      className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                    />
                  </div>
                </div>

                {/* row 4 bureau, role*/}
                <div className="mb-4.5 flex flex-col gap-6 xl:flex-row">
                  <CustomSelectBureau
                    required={true}
                    inputLabel="Bureau de l'employe"
                    inputPlaceholder="Saisir un bureau"
                    wrapperStyle="w-full xl:w-1/2"
                    labelStyle="mb-2.5 block text-black dark:text-white"
                    inputStyle="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                    maxHeightList={300}
                    matchList={bureaux}
                    selectOptionEvent={handleOptionSelect}
                    typingInputEvent={handleTypingInput}
                  />

                  <CustomSelectMultiRoles
                    required={true}
                    inputLabel="Roles de l'employe"
                    inputPlaceholder="Saisir un role"
                    wrapperStyle="w-full xl:w-1/2"
                    labelStyle="mb-2.5 block text-black dark:text-white"
                    inputStyle="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                    maxHeightList={100}
                    selectOptionEvent={handleRoleSelect}
                    typingInputEvent={handleTypingRole}
                    matchList={roles}
                  />
                </div>

                {/* row 5 create | update, annuler */}
                <div className="form-actions bg-green-600">
                  <button
                    onClick={props.onClose}
                    className="ml-2 flex w-1/4 justify-center rounded bg-secondary p-3 font-medium text-white"
                  >
                    <span className="mt-1">
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                        className="button-icon"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M6 18L18 6M6 6l12 12"
                        />
                      </svg>
                    </span>
                    Annuler
                  </button>

                  {props.mode !== MODAL_MODE.view &&
                    props.mode === MODAL_MODE.create ? (
                    <button
                      onClick={handleCreate}
                      disabled={isLoading}
                      className={`ml-2 flex w-1/4 justify-center rounded bg-success p-3 font-medium text-white ${isEmpty && 'cursor-not-allowed hover:bg-graydark'}`} title={` ${isEmpty ? 'Les champs ne doivent pas etre vide' : 'Valider'}`}
                    >
                      <span className="mt-1 mr-2">
                        {isLoading ? (
                          <Indicator widtf={5} height={5} border="white" />
                        ) : (
                          <NewIcon size={2} color="#fff" />
                        )}
                      </span>
                      Créer
                    </button>
                  ) : null}
                  {props.mode !== MODAL_MODE.view &&
                    props.mode === MODAL_MODE.update ? (
                    <button
                      onClick={handleUpdate}
                      disabled={isLoading}
                      className={`ml-2 flex w-1/4 justify-center rounded bg-success p-3 font-medium text-white`}
                    >
                      <span className="mt-1 mr-2">
                        {isLoading ? (
                          <Indicator widtf={5} height={5} border="white" />
                        ) : (
                          <EditIcon color="#fff" size={18} />
                        )}
                      </span>
                      Enregistrer
                    </button>
                  ) : null}
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default CreateOrUpdateUserModal;