import React from 'react';

const Modal = (props) => {
	return (
		<>
			<button type="button" className={props.btnClasses} data-toggle="modal" data-target="#modal">
				{props.btnText}
			</button>

			<div className="modal fade" id="modal" tabIndex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
				<div className="modal-dialog" role="document">
					<div className="modal-content">
						<div className="modal-header">
							<h5 className="modal-title" id="modalLabel">{props.modalTitle}</h5>
							<button type="button" className="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div className="modal-footer">
							<button type="button" className={props.modalSecondaryBtnClasses} data-dismiss="modal">
								{props.modalSecondaryBtnText}
							</button>

							<button type="button" className={props.modalPrimaryBtnClasses ?? 'btn btn-primary'}
								data-dismiss="modal" onClick={props.onModalPrimaryBtnClick}>
								{props.modalPrimaryBtnText}
							</button>
						</div>
					</div>
				</div>
			</div>
		</>
	);
};

export default Modal;