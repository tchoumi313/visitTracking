import {Link} from "react-router-dom";
import NotFoundSVG from '../../images/error/not found img.svg'

const NotFound = ()=>{

  return(
    <div className='w-full h-full align-items-center justify-center justify-content-center'>
      <div className="no-scrollbar rounded-sm mx-[15%] my-[5.5%] align-items-center justify-center justify-content-center border border-stroke bg-stroke shadow-xl shadow-graydark dark:border-strokedark dark:bg-boxdark">
        <div className="flex flex-wrap items-center h-full">
          <div className="w-full flex">
            <div className={'h-full w-[50%] xl:block xl:w-1/2'}>
              <img src={NotFoundSVG} alt="Not Found Image" className="" />
            </div>
            <div className="items-center justify-center w-[50%] py-[15%]">
              <div className="text-center self-center bg-center">
                <h1 className="mb-6 px-[7%] font-bold text-2xl text-black dark:text-white">
                  Sorry!!! The Page Can't Be Found
                </h1>

                <div className={'text-center bg-center flex'}>
                  <div className="ml-[20%]"></div>
                  <Link
                    to="/"
                    className="flex text-center self-center bg-center items-center py-3 border-stroke text-white font-bold rounded-xl bg-primary transition hover:bg-opacity-95 w-full"
                  >
                    {/* Svg */}
                    <svg className="fill-current ml-[20%] pt-[0.5px] w-4 h-4">
                      <path d="M14.7492 6.38125H2.73984L7.52109 1.51562C7.77422 1.2625 7.77422 0.86875 7.52109 0.615625C7.26797 0.3625 6.87422 0.3625 6.62109 0.615625L0.799219 6.52187C0.546094 6.775 0.546094 7.16875 0.799219 7.42188L6.62109 13.3281C6.73359 13.4406 6.90234 13.525 7.07109 13.525C7.23984 13.525 7.38047 13.4687 7.52109 13.3562C7.77422 13.1031 7.77422 12.7094 7.52109 12.4563L2.76797 7.64687H14.7492C15.0867 7.64687 15.368 7.36562 15.368 7.02812C15.368 6.6625 15.0867 6.38125 14.7492 6.38125Z" fill=""></path>
                    </svg>
                    {/* End Svg */}
                    <div className="flex flex-1 text-center justify-start items-start m-1 ml-[4%] font-semibold dark:text-white text-xl">
                      <h2>
                        Back to Home
                      </h2>
                    </div>
                  </Link>
                  <div className="ml-[20%]"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

        
    );
};

export default NotFound;