import { useEffect, useState } from "react";
import { MODAL_MODE } from "../../../constants/APP_CONSTANTS";
import { EditIcon, NewIcon } from "../../../constants/Icon";
import { BureauRequest, BureauResponse } from "../../../generated/models";
import { TOKEN_LOCAL_STORAGE_KEY } from "../../../constants/LOCAL_STORAGE";
import { BureauControllerApi } from "../../../generated";
import { useDispatch, useSelector } from "react-redux";
import { ReduxProps } from "../../../redux/configureStore";
import Indicator from "../../Authentication/components/Indicator";
import { createBureau, updateBureau } from "../../../redux/Actions/BureauAction";

interface ModalProps {
  mode: MODAL_MODE,
  title: string,
  onClose: () => void,
  refresh?: () => void,
  item?: BureauResponse | null,

  setShowSuccessNotif: (value: boolean) => void,
  setSuccessNotifMessage: (value: string) => void,
  setSuccessNotifDescription: (value: string | null) => void,

  setShowWarning: (value: boolean) => void,
  setWarningMessage: (value: string) => void,
  setWarningNotifDescription: (value: string | null) => void,
}

const CreateOrUpdateBureauModal: React.FC<ModalProps> = (props) => {
  const dispatch = useDispatch();

  const [formData, setFormData] = useState({
    batiment: props.item?.batiment ? props.item.batiment : '',
    etage: props.item?.etage ? props.item.etage : '',
    porte: props.item?.porte ? props.item.porte : '',
  });

  const isEmpty = !formData.batiment || !formData.etage || !formData.porte

  const state = useSelector((state: ReduxProps) => state);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  // Errors while Creating or Updating
  const [isError, setIsError] = useState(false);

  const handleInputChange = (e: any) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape") {
        props.onClose();
      } else if (event.key === "enter") {
        event.preventDefault()
        if (props.mode === MODAL_MODE.create) {
          handleCreate
        } else if (props.mode === MODAL_MODE.update) {
          handleUpdate
        }
      }
    };
    document.addEventListener("keydown", handleKeyDown);
    return () => { document.removeEventListener("keydown", handleKeyDown); };
  }, [props]);

  const handleCreate = (_event: any) => {
    _event.preventDefault() // stopper la soumissoin par defaut du formulaire...

    const token: string = localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY)!;
    const bureauxApi = new BureauControllerApi({ ...state.environment, accessToken: token });

    setIsLoading(true)

    const apiParams: BureauRequest = {
      batiment: formData.batiment.toUpperCase(),
      etage: formData.etage.toUpperCase(),
      porte: formData.porte.toUpperCase(),
    }

    bureauxApi.create4(apiParams)
      .then((response: any) => {
        if (response.status === 201) {
          props.onClose()
          if (props.refresh) props.refresh()

          // notification
          props.setSuccessNotifMessage('Succes')
          props.setSuccessNotifDescription('Un nouveau bureau viens d\'etre rajoute au catalogue avec success ! ')
          props.setShowSuccessNotif(true)
          dispatch(createBureau(formData))
        }
      })
      .catch((err) => {
        console.log(err)
        setIsError(true);
        if (err.response && err.response.status === 400) {
          // props.setShowWarning(true);
          // props.setWarningMessage('Mauvaise');
          // props.setWarningNotifDescription(JSON.stringify(err.response.data));
          alert(err.response)
        } else {
          if (err.response.status === 403) {
            props.setShowWarning(true);
            props.setWarningMessage("Probleme de token");
            props.setWarningNotifDescription("Votre token n\'est plus valide vous allez etre deconnecte")
          }
        }
      })
      .finally(() => {
        setIsLoading(false)

        // notification
        setTimeout(() => {
          props.setShowSuccessNotif(false)
        }, 4000);
      });
  }

  const handleUpdate = (_event: any) => {
    _event.preventDefault() // stopper la soumissoin par defaut du formulaire...

    const token: string = localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY)!;
    const bureauxApi = new BureauControllerApi({ ...state.environment, accessToken: token });

    setIsLoading(true)

    const apiParams: BureauRequest = {
      batiment: formData.batiment.toUpperCase(),
      etage: formData.etage.toUpperCase(),
      porte: formData.porte.toUpperCase(),
    }

    bureauxApi.update4(apiParams, props.item?.id!)
      .then((response) => {
        if (response.status === 202) {
          props.onClose()
          if (props.refresh) props.refresh()

          // notification
          props.setSuccessNotifMessage("Bureau Modifie avec succes")
          props.setSuccessNotifDescription('Ce Bureau a ete correctement mise a jour, veuillez consulter le catalogue !')
          props.setShowSuccessNotif(true)
          dispatch(updateBureau(formData))
        }
      })
      .catch((err) => {
        setIsError(true);
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
          props.setShowWarning(false);
          props.setShowSuccessNotif(false)
        }, 4000);
      });

  }


  return (
    <div
      id="authentication-modal"
      className="authentication-modal"
      onClick={props.onClose}
    >
      <div className="modal-container relative items-center justify-center mx-auto top-modal-animation lg:left-[10%] lg:top-[8%]" onClick={(event) => event.stopPropagation()}>
        <div className="modal-content bg-white rounded-sm border border-stroke shadow-default dark:border-strokedark dark:bg-boxdark">
          <button onClick={props.onClose} className="flex">
            <svg aria-hidden="true" className="close-icon" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
              <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
            </svg>
            <span className="sr-only w-20 h-5 dark:text-stroke">Close modal</span>
          </button>
          <div className="modal-body">

            <div className="border-b border-stroke pb-2 px-6.5 -mt-10 dark:border-strokedark">
              <h2 className=" text-size-[1.25rem] font-medium text-black dark:text-white">
                {props.title}
              </h2>
            </div>
            <form /*onSubmit={handleSubmit}*/ className="pt-1">
              <div className="form-group">
                <label htmlFor="batiment" className="form-label form-class mb-2.5 flex text-black dark:text-white">
                  Batiment <span className="text-meta-1">*</span>
                </label>
                <input
                  type="text"
                  name="batiment"
                  id="batiment"
                  value={formData.batiment}
                  disabled={props.mode === MODAL_MODE.view ? true : false}
                  onChange={handleInputChange}
                  placeholder="Entrer le batiment du bureau"
                  className={`form-input form-class w-full rounded-lg border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary dark:disabled:bg-black dark:text-white ${props.mode === MODAL_MODE.view ? 'disabled-input' : ''}`}
                />
              </div>
              <div className="form-group">
                <label htmlFor="etage" className="form-label form-class mb-2.5 flex text-black dark:text-white">
                  Etage <span className="text-meta-1">*</span>
                </label>
                <input
                  type="text"
                  id="etage"
                  name="etage"
                  value={formData.etage}
                  disabled={props.mode === MODAL_MODE.view ? true : false}
                  onChange={handleInputChange}
                  placeholder="Entrer l'etage du bureau"
                  className={`form-input form-class w-full rounded-lg border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary dark:disabled:bg-black dark:text-white ${props.mode === MODAL_MODE.view ? 'disabled-input' : ''}`}
                />
              </div>
              <div className="form-group">
                <label htmlFor="porte" className="form-label form-class mb-2.5 flex text-black dark:text-white">
                  Porte <span className="text-meta-1">*</span>
                </label>
                <input
                  type="text"
                  id="porte"
                  name="porte"
                  value={formData.porte}
                  disabled={props.mode === MODAL_MODE.view ? true : false}
                  onChange={handleInputChange}
                  placeholder="Entrer le numero de la porte"
                  className={`form-input form-class w-full rounded-lg border-[1.5px] border-stroke bg-transparent py-3 px-5 font-medium outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:focus:border-primary dark:disabled:bg-black dark:text-white ${props.mode === MODAL_MODE.view ? 'disabled-input' : ''}`}
                />
              </div>
              {isError && <p className="text-danger"></p>}
              <div className="form-actions">
                <button onClick={props.onClose} className="cancel-button">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" className="button-icon">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                  </svg>
                  Annuler
                </button>
                {props.mode !== MODAL_MODE.view && props.mode === MODAL_MODE.create ?
                  <button onClick={handleCreate} className={`create-button ${isEmpty && 'cursor-not-allowed hover:bg-graydark'}`} title={` ${isEmpty ? 'Les champs ne doivent pas etre vide' : 'Valider'}`} >
                    {isLoading ? <Indicator widtf={5} height={5} border="white" /> : <NewIcon size={2} color="#fff" />}
                    <span className="ml-2">Créer </span>
                  </button> : null}
                {props.mode !== MODAL_MODE.view && props.mode === MODAL_MODE.update ?
                  <button onClick={handleUpdate} className={`create-button ${isEmpty && 'cursor-not-allowed hover:bg-graydark'}`} title={` ${isEmpty ? 'Les champs ne doivent pas etre vide' : 'Valider'}`} >
                    <EditIcon color="#fff" size={18} />
                    &nbsp;Enregistrer
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

export default CreateOrUpdateBureauModal;