import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { doRequest } from '../../../Utils';
import Modal from '../../../components/Modal';
import EditOpinionForm from '../Components/EditOpinionForm';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const EditableOpinion = (props) => {

    const history = useHistory();
    const opinionInfo = props.opinionInfo;

    const deleteOpinionOnClick = async opinionId => {
        try {
            const action = async () => axios.delete('/api/opinions', {
                data: { opinionId: opinionId },
                validateStatus: false,
                headers: { 'X-Auth-Token': props.auth.token }
            });
            await doRequest(action);

            history.go();
        } catch (error) {
            alert(`${error} error occured`);
        }
    }
    const onFinishEditCb = (newContent) => {
        setInEditMode(false);
        setContent(newContent);
    }

    const [inEditMode, setInEditMode] = useState(false);
    const [content, setContent] = useState(opinionInfo.opinion.content);

    if (!inEditMode) return <>
        <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`o-${opinionInfo.opinion.id}`}>
            <p className="mb-1">
                <b>{opinionInfo.user.firstName} {opinionInfo.user.lastName}</b> ({opinionInfo.user.email}) wrote:
			</p>

            <p>{content}</p>

            <button className="btn btn-primary w-25" onClick={() => setInEditMode(true)}>
                Edit
			</button>

            <Modal title={"Are you sure"}
                btnText={"Delete"}
                btnClasses={"btn btn-danger w-25"}
                modalTitle={"Are you sure?"}
                modalPrimaryBtnText={"Delete"}
                modalPrimaryBtnClasses={"btn btn-danger"}
                onModalPrimaryBtnClick={async () => deleteOpinionOnClick(opinionInfo.opinion.id)}
                modalSecondaryBtnText={"Cancel"}
                modalSecondaryBtnClasses={"btn btn-secondary"}
            />
        </div>
    </>

    else return <>
        <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`o-${opinionInfo.opinion.id}`}>
            <p className="mb-1">
                <b>{opinionInfo.user.firstName} {opinionInfo.user.lastName}</b> ({opinionInfo.user.email}) wrote:
			</p>

            <EditOpinionForm opinion={opinionInfo.opinion}
                auth={props.auth}
                onFinishEditCb={newContent => onFinishEditCb(newContent)} />
        </div>
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(EditableOpinion);