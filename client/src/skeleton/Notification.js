import React from 'react';
import { Link } from 'react-router-dom';
import notificationIcon from '../../src/assets/img/notification.svg';
import AwareComponentBuilder from '../common/AwareComponentBuilder';

const Notification = (props) => {
    return <>
        <Link to={{ pathname: '/notifications' }} className="bg-blue border-darken-2 mr-2" style={{ cursor: "pointer" }} >
            <img src={notificationIcon} style={{ width: "25px", opacity: "0.4", paddingTop: "7px" }} />

            {
                props.notifs?.length > 0 &&
                <span className="badge badge-danger">
                    {props.notifs.length}
                </span>
            }
        </Link>
    </>
};

export default new AwareComponentBuilder()
    .withNotifsAwareness()
    .build(Notification);