import { connect, useDispatch, useSelector } from 'react-redux';
import { VisiteControllerApi, VisiteResponse } from '../../../generated';
import { ReduxProps } from '../../../redux/configureStore';
import { useCallback, useEffect, useState } from 'react';
import { MODAL_MODE } from '../../../constants/APP_CONSTANTS';
import { Link } from 'react-router-dom';
import { EditIcon, NewIcon, NextIcon, PreviousIcon, TrashIcon } from '../../../constants/Icon';

import CreateOrUpdateVisiteModal from './CreateOrUpdateVisiteModal';

import { TOKEN_LOCAL_STORAGE_KEY } from '../../../constants/LOCAL_STORAGE';
import { DeleteItemModal } from '../../../constants/DeleteItemModal';
import { GridIndicator } from '../../../constants/GridIndicator';

import './DisplayVisites.css'

interface DisplayVisitesProps {
  visites: VisiteResponse[],
  isLoading: boolean,

  setShowSuccessNotif: (value: boolean) => void,
  setSuccessNotifMessage: (value: string) => void,
  setSuccessNotifDescription: (value: string | null) => void,

  setShowWarning: (value: boolean) => void,
  setWarningMessage: (value: string) => void,
  setWarningNotifDescription: (value: string | null) => void,

  setShowDangerNotif: (value: boolean) => void,
  setDangerNotifMessage: (value: string) => void,
  setDangerNotifDescription: (value: string | null) => void,
}

import { DangerNotification } from '../../../services/Notification.service';
import ReactPaginate from 'react-paginate';

const DisplayVisites: React.FC<DisplayVisitesProps> = (props) => {
  const state = useSelector((state: ReduxProps) => state);
  const [listeVisites, setListeVisites] = useState<VisiteResponse[]>(props.visites);
  const [visiteS, setVisiteS] = useState<VisiteResponse | null>(props.visites[0]);
  const [selectedVisite, setSelectedVisite] = useState<VisiteResponse>({});
  // Pagination
  const [currentPage, setCurrentPage] = useState(0);
  const visitesPerPage = 10;

  const offset = currentPage * visitesPerPage;
  const currentVisites = listeVisites.slice(offset, offset + visitesPerPage);

  const pageCount = Math.ceil(listeVisites.length / visitesPerPage);

  const handlePageClick = (data: { selected: any; }) => {
    const selected = data.selected;
    setCurrentPage(selected);
  };
  // End Pagination

  const [showCreateOrUpdateModal, setShowCreateOrUpdateModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  const [modalMode, setModalMode] = useState<MODAL_MODE>(MODAL_MODE.create);
  const [modalTitle, setModalTitle] = useState<string>("");
  const [showIndicator, setShowIndicator] = useState<boolean>(false);
  const [isError, setIsError] = useState(false);
  const [isErrorDescription, setIsErrorDescription] = useState('');

  const dispatch = useDispatch();

  const onReady = useCallback(() => {
    const token: string = localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY)!;
    const visitesApi = new VisiteControllerApi({ ...state.environment, accessToken: token });

    setShowIndicator(true)

    visitesApi.indexVisite()
      .then((response) => {
        if (response && response.data) {
          if (response.status === 200) {
            setListeVisites(response.data);
          }
        }
      })
      .catch((error) => {
        setIsError(true);
        console.log(isError)
        if (error?.response && error?.response?.data){
          setIsErrorDescription(error?.response?.data?.message);
        } else {
          if (error.response && error.response.status === 403) {
            setIsErrorDescription('Vous n\'etes pas autorises');
          } else {
            setIsErrorDescription('Probleme lors de la recuperation des visites')
        } }
        
        // Handle Warning Notif 
        // props.setShowWarning(true)
      })
      .finally(() => {
        setShowIndicator(false)
        setTimeout(() => {
          setIsError(false)
          props.setShowSuccessNotif(false)
        }, 3000);
      });
  }, [dispatch]);

  useEffect(() => {
    onReady();

    setShowDeleteModal(false);
  }, []);

  //Create Visite
  const handleNewItem = () => {
    setVisiteS(null);
    setShowCreateOrUpdateModal(true);
    setModalMode(MODAL_MODE.create)
    setModalTitle("Créer une nouvelle visite")
  }

  const handleCloseCreateOrUpdateModal = () => {
    setShowCreateOrUpdateModal(false)
  }
  // Update Visite
  const handleEdit = (visiteSelected: VisiteResponse) => {
    setVisiteS(visiteSelected);
    setModalMode(MODAL_MODE.update)
    setModalTitle("Modifier la visite")
    setShowCreateOrUpdateModal(true);
  };

  // Delete Visite
  const handleDelete = (visite: VisiteResponse) => {
    setSelectedVisite(visite);
    setShowDeleteModal(true)
  };

  const handleCloseDeleteModal = () => {
    setShowDeleteModal(false)
  }

  const handleConfirmDeletingModal = () => {
    proccessDeleteItem()
  }

  const proccessDeleteItem = () => {
    setShowDeleteModal(false)

    const token: string = localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY)!;
    const visitesApi = new VisiteControllerApi({ ...state.environment, accessToken: token });

    setShowIndicator(true)

    visitesApi.delete1(selectedVisite.id!)
      .then((response) => {
        if (response) {
          if (response.status === 204) {
            console.log('response :', response)
            setShowDeleteModal(false)
            props.setSuccessNotifMessage("Succes")
            props.setSuccessNotifDescription('Cette visite a ete supprime avec success')
            props.setShowSuccessNotif(true)
          }
        }
      })
      .catch((error) => {
        setIsError(true);
        if (error?.response && error?.response?.data){
          setIsErrorDescription(error?.response?.data?.message);
        } else {
          if (error.response && error.response.status === 403) {
            setIsErrorDescription('Vous n\'etes pas autorises');
          } else {
            setIsErrorDescription('Probleme lors de la suppression de la visite')
        } }
      })
      .finally(() => {

        setShowIndicator(false)

        // notification
        setTimeout(() => {
          props.setShowSuccessNotif(false)
          props.setShowDangerNotif(false)
          setIsError(false)
        }, 3000);
      });

  }

  const shared_class: string = 'rounded-md inline-flex items-center justify-center gap-2.5 py-2 sm:py-2  px-1 text-center font-medium text-white hover:bg-opacity-90 sm:px-2 md:px-3 lg:px-3 xl:px-3'

  return (
    <div>
      <div className={`absolute bg-black w-[90%]`}>
        {isError && <DangerNotification 
          message={'Error'}
          description={isErrorDescription}
        />}
      </div>
      
      <div className="mb-3.5 flex flex-wrap gap-1 xl:gap-3 justify-end">
        <Link onClick={handleNewItem} to="#" className={`${shared_class} hover:opacity-80`} style={{ backgroundColor: '#057a4f' }}>
          <div className={'w-5 h-5 -mr-1'}>
            <NewIcon size={2} color='#fff' />
          </div>
          Nouveau
        </Link>
      </div>

      {showCreateOrUpdateModal && <CreateOrUpdateVisiteModal
        mode={modalMode}
        title={modalTitle}
        onClose={handleCloseCreateOrUpdateModal}
        refresh={onReady}
        item={modalMode !== MODAL_MODE.create ? visiteS : null}
        setShowSuccessNotif={props.setShowSuccessNotif}
        setSuccessNotifMessage={props.setSuccessNotifMessage}
        setSuccessNotifDescription={props.setSuccessNotifDescription}
        setShowWarning={props.setShowWarning}
        setWarningMessage={props.setWarningMessage}
        setWarningNotifDescription={props.setWarningNotifDescription}
      />}
      {showDeleteModal && <DeleteItemModal
        isVisible={true}
        itemName={'Visite d\'id ' + selectedVisite.id}
        onClose={handleCloseDeleteModal}
        refresh={onReady}
        onConfirm={handleConfirmDeletingModal}
      />}

      <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
        <div className="max-w-full overflow-x-auto">
          <table className="w-full table-auto">
            <thead>
              <tr className="bg-gray-2 text-left dark:bg-meta-4 border-b">
                <th className="py-4 px-4 font-medium text-black dark:text-white">
                  Visiteur
                </th>
                <th className="py-4 px-4 font-medium text-black dark:text-white">
                  Employe
                </th>
                <th className="py-4 px-4 font-medium text-black dark:text-white">
                  Motif
                </th>
                <th className="py-4 px-4 font-medium text-black dark:text-white">
                  Date de Visite
                </th>
                <th className="py-4 px-4 font-medium text-black dark:text-white">
                  Heure de Fin
                </th>
                <th className="py-4 px-4 font-medium text-black dark:text-white">
                  Heure de Debut
                </th>
                <th className="py-4 px-4 font-medium text-black dark:text-white">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody>
              {showIndicator && <GridIndicator />}
              {listeVisites.length === 0 ? (
                <tr className='border-b w-full'>
                  <td colSpan={8} className='text-center py-2'>Aucune visite pour le moment</td>
                </tr>
              ) : (
                currentVisites!.map((item) => (
                  <tr key={item.id} className='border-b'>
                    <td className="py-2 px-2">
                      <p className="text-black dark:text-white">{item.visiteur?.nom + " " + item.visiteur?.prenom}</p>
                    </td>
                    <td className="py-2 px-2">
                      <p className="text-black dark:text-white">{item.user?.nom + " " + item.user?.prenom}</p>
                    </td>
                    <td className="my-2 px-2">
                      <p className="text-black dark:text-white">{item.motif}</p>
                    </td>
                    <td className="py-2 px-2">
                      <p className="text-black dark:text-white">{new Date(item.dateVisite!).toLocaleDateString()!}</p>
                    </td>
                    <td className="my-2 px-2">
                      <p className="text-black dark:text-white">{item.heureDebut!}</p>
                    </td>
                    <td className="py-2 px-2">
                      <p className="text-black dark:text-white">{item.heureFin!}</p>
                    </td>
                    <td className="py-2 px-5">
                      <div className="flex items-center space-x-4">
                        <button className="hover:text-primary" title='Supprimer' onClick={() => handleDelete(item)}>
                          <TrashIcon size={17} />
                        </button>
                        <button
                          className="hover:text-primary"
                          title="Modifier"
                          onClick={() => handleEdit(item)}
                        >
                          <EditIcon size={17} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
          <ReactPaginate
            className={`inline-flex mt-3 justify-end w-full font-bold pr-5`}
            previousLabel={<PreviousIcon size={15} class='mt-1 hover:opacity-90 hover:text-primary mr-2' /> }
            nextLabel={<NextIcon size={15} class='mt-1 hover:opacity-90 hover:text-primary ml-2'/>}
            breakLabel={'...'}
            pageCount={pageCount}
            marginPagesDisplayed={5}
            pageRangeDisplayed={5}
            onPageChange={handlePageClick}
            containerClassName={'pagination'}
            activeClassName={'text-primary border-t-2 bg-gray dark:bg-graydark'}
            pageLinkClassName={'hover:opacity-90 hover:text-primary hover:bg-gray  dark:hover:bg-graydark hover:border-t-2 px-3'}
          />
      </div>
    </div>
  );
};

function mapStateToProps(state: ReduxProps): ReduxProps {
  return {
    user: state.user,
    environment: state.environment,
    loggedIn: state.loggedIn,
    access_token: state.access_token,
  };
}
export default connect(mapStateToProps)(DisplayVisites)
