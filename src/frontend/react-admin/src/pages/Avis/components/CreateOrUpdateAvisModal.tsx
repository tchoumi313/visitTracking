import { useEffect, useState } from "react";
import { MODAL_MODE } from "../../../constants/APP_CONSTANTS";
import { EditIcon, NewIcon } from "../../../constants/Icon";
import { UserResponse, AvisRequest, AvisResponse, VisiteResponse } from "../../../generated/models";
import { TOKEN_LOCAL_STORAGE_KEY } from "../../../constants/LOCAL_STORAGE";
import { AvisControllerApi, VisiteControllerApi } from "../../../generated";
import { useSelector } from "react-redux";
import { ReduxProps } from "../../../redux/configureStore";
import Indicator from "../../Authentication/components/Indicator";
import CustomSelectVisite from '../../../components/CustomSelects/CustomSelectVisite/CustomSelectVisite';

interface ModalProps {
  mode: MODAL_MODE,
  title: string,
  onClose: () => void,
  refresh?: () => void,
  item?: AvisResponse | null,

  setShowSuccessNotif: (value: boolean) => void,
  setSuccessNotifMessage: (value: string) => void,
  setSuccessNotifDescription: (value: string | null) => void,

  setShowWarning: (value: boolean) => void,
  setWarningMessage: (value: string) => void,
  setWarningNotifDescription: (value: string | null) => void,
}

const CreateOrUpdateAvisModal: React.FC<ModalProps> = (props) => {

  const state = useSelector((state: ReduxProps) => state);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const [formData, setFormData] = useState<AvisRequest>({
    libelle: props.item?.libelle!,
    visite: props.item?.visite!,
  });
  
  const isEmpty = !formData.libelle || !formData.visite
  
  // Option select for Visite 
  const [visites, setVisites] = useState<VisiteResponse[]>([])

  const handleTypingVisite = (keyword: string) => {
    if (keyword.length > 0) {
      const token: string = localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY)!;
      const visiteApi = new VisiteControllerApi({
        ...state.environment,
        accessToken: token,
      });

      visiteApi
        .sort(keyword)
        .then((response) => {
          if (response.status === 200) {
            setVisites(response.data);
          }
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => { });
    }
  };

  const handleVisiteSelect = (item: UserResponse) => {
    setFormData((prevValues) => ({
      ...prevValues,
      visite: item,
    }));
  };

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
    const avisApi = new AvisControllerApi({ ...state.environment, accessToken: token });

    const newVisite: VisiteResponse = {
      id: formData.visite?.id,
    }
    setIsLoading(true)

    const apiParams: AvisRequest = {
      libelle: formData.libelle,
      visite: newVisite,
    }
    console.log(apiParams)

    avisApi.create5(apiParams)
      .then((response) => {
        if (response && response.data) {
          if (response.status === 201) {
            props.onClose()
            if (props.refresh) props.refresh()

            // notification
            props.setSuccessNotifMessage('Succes')
            props.setSuccessNotifDescription('Un nouveau Rende-vous viens d\'etre rajoute au catalogue avec succes ! ')
            props.setShowSuccessNotif(true)
          }
        }
      })
      .catch((error) => {
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
    const avisApi = new AvisControllerApi({ ...state.environment, accessToken: token });

    setIsLoading(true)

    const apiParams: AvisRequest = {
      ...formData
    }

    avisApi.update5(apiParams, props.item?.id!)
      .then((response) => {
        if (response && response.data) {
          console.log(response.data);

          if (response.status === 202) {
            props.onClose()
            if (props.refresh) props.refresh()

            // notification
            props.setSuccessNotifMessage('Succes')
            props.setSuccessNotifDescription('Ce Rende-vous a ete correctement mise a jour, veuillez consulter le catalogue !')
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
      className="authentication-modal"
      onClick={props.onClose}
    >
      <div className="modal-container relative items-center justify-center mx-auto top-modal-animation mt-20 max-xl:mt-[15%] lg:ml-[30%] lg:mt-[20%] xl:mt-auto xl:ml-auto" onClick={(event) => event.stopPropagation()}>
        <div className="modal-content bg-white rounded-sm border-stroke shadow-default dark:border-strokedark dark:bg-boxdark">
          <button onClick={props.onClose} className="flex">
            <svg aria-hidden="true" className="close-icon" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
              <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
            </svg>
            <span className="sr-only">Close modal</span>
          </button>
          <div className="modal-body">

            <div className="border-b border-stroke py-2 px-6.5 dark:border-strokedark -mt-6">
              <h2 className="text-size-[1.25rem] font-medium text-black dark:text-white">
                {props.title}
              </h2>
            </div>
            <form className="modal-form">
              <div>
                {/* Row 1 Motif */}
                <div className="form-group">
                  <label htmlFor="libelle" className="form-label form-class mb-2.5 block text-black dark:text-white">
                    Libelle <span className="text-meta-1">*</span>
                  </label>
                  <textarea
                    id="libelle"
                    name="libelle"
                    value={formData.libelle}
                    onChange={handleInputChange}
                    placeholder="Entrer le libelle de l'avis"
                    rows={2}
                    className={`form-input form-class w-full rounded-lg border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary dark:disabled:bg-black dark:text-white ${props.mode === MODAL_MODE.view ? 'disabled-input' : ''}`}
                  />
                </div>
                {/* row 3 visite */}
                <div className="mb-4.5 flex flex-col gap-6 xl:flex-row">
                  <CustomSelectVisite
                    required={true}
                    inputLabel="Visite"
                    inputPlaceholder="Saisir le motif d'une visite"
                    wrapperStyle="w-full"
                    labelStyle="mb-2.5 block text-black dark:text-white"
                    inputStyle="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary"
                    maxHeightList={300}
                    matchList={visites}
                    selectOptionEvent={handleVisiteSelect}
                    typingInputEvent={handleTypingVisite}
                  />
                </div>
              </div>
              <div className="form-actions bg-green-600">
                <button onClick={props.onClose} className={`cancel-button`}>
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" className="button-icon">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                  </svg>
                  Annuler
                </button>
                {props.mode !== MODAL_MODE.view && props.mode === MODAL_MODE.create ?
                  <button onClick={handleCreate} className={`create-button ${isEmpty && 'cursor-not-allowed hover:bg-graydark'}`} title={` ${isEmpty ? 'Les champs ne doivent pas etre vide' : 'Valider'}`}>
                    {isLoading ? <Indicator widtf={5} height={5} border="white" /> : <NewIcon size={2} color="#fff" />}
                    <span className="ml-2">Créer </span>
                  </button> : null}
                {props.mode !== MODAL_MODE.view && props.mode === MODAL_MODE.update ?
                  <button onClick={handleUpdate} className="create-button">
                    {isLoading ? <Indicator widtf={5} height={5} border="white" /> : <EditIcon color="#fff" size={18} />}                    
                    Enregistrer
                  </button> : null
                }
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  )
}

export default CreateOrUpdateAvisModal;